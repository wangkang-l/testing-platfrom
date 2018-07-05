package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.*;
import com.bgw.testing.server.service.CaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@Api(description = "用例管理")
@RequestMapping(value = AppConst.BASE_PATH)
public class CaseController {

    @Autowired
    private CaseService caseService;

    @ApiOperation(value = "校验测试用例")
    @RequestMapping(value = "/case/validation", method = RequestMethod.POST)
    public Boolean verifyCase(@RequestBody @Valid CaseInfoDto caseInfoDto) {
        try {
            caseInfoDto.check();
        } catch (Exception e) {
            throw new RuntimeException("用例校验失败:" + e.getMessage());
        }
        return true;
    }

    @ApiOperation(value = "用例查询")
    @RequestMapping(value = "/case/{group_id}", method = RequestMethod.GET)
    public PageInfo<CaseInfoDto> getCaseInfo(@PathVariable(value = "group_id") String groupId,
                                             @RequestParam(value = "page_num") Integer pageNum,
                                             @RequestParam(value = "page_size") Integer pageSize,
                                             @RequestParam(required = false) String var4) {
        return caseService.getCaseInfo(groupId, pageNum, pageSize, var4);
    }

    @ApiOperation(value = "新增用例信息")
    @RequestMapping(value = "/case", method = RequestMethod.POST)
    public Boolean addCaseInfo(@RequestBody @Valid CaseInfoDto caseInfoDto) {
        caseService.addCaseInfo(caseInfoDto);
        return true;
    }

    @ApiOperation(value = "更新用例信息")
    @RequestMapping(value = "/case", method = RequestMethod.PUT)
    public Boolean updateCaseInfo(@RequestBody @Valid CaseInfoDto caseInfoDto) {
        caseService.updateCaseInfo(caseInfoDto);
        return true;
    }

    @ApiOperation(value = "删除用例信息")
    @RequestMapping(value = "/case/{group_id}/{case_id}", method = RequestMethod.DELETE)
    public Boolean deleteCaseInfo(@PathVariable(value = "group_id") String groupId, @PathVariable(value = "case_id") String caseId) {
        caseService.deleteCaseInfo(groupId, caseId);
        return true;
    }

    @ApiOperation(value = "指定用例内容执行")
    @RequestMapping(value = "/case/operation", method = RequestMethod.POST)
    public List<CaseReportDto> runCase(@RequestBody @Valid List<CaseInfoDto> caseInfoDtos) {

        return caseService.runCase(caseInfoDtos);
    }

    @ApiOperation(value = "指定组ID执行")
    @RequestMapping(value = "/case/operation/{group_id}", method = RequestMethod.POST)
    public ReportInfoDto runCase(@PathVariable(value = "group_id") String groupId) {
        return caseService.runCase(groupId);
    }

    @ApiOperation(value = "移动测试用例")
    @RequestMapping(value = "/case", method = RequestMethod.PATCH)
    public Boolean moveCase(
            @RequestParam(value = "case_ids") List<String> caseIds,
            @RequestParam(value = "new_group_id") String newGroupId) {
        caseService.moveCaseInfo(caseIds, newGroupId);
        return true;
    }

}
