package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.CaseInfoDto;
import com.bgw.testing.common.dto.PageInfo;
import com.bgw.testing.server.service.CaseService;
import com.bgw.testing.server.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Api(description = "用例管理")
@RequestMapping(value = AppConst.BASE_PATH + "case")
public class CaseController {

    @Autowired
    private CaseService caseService;
    @Autowired
    private RedisService redisService;

    @ApiOperation(value = "根据组ID获取用例信息")
    @RequestMapping(value = "/info/{group_id}", method = RequestMethod.GET)
    public PageInfo<CaseInfoDto> getCaseInfo(@PathVariable(value = "group_id") String groupId,
                                             @RequestParam(value = "page_num") Integer pageNum,
                                             @RequestParam(value = "page_size") Integer pageSize) {
        return caseService.getCaseInfo(groupId, pageNum, pageSize);
    }

    @ApiOperation(value = "新增用例信息")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public boolean addCaseInfo(@RequestBody CaseInfoDto caseInfoDto) {
        caseService.addCaseInfo(caseInfoDto);
        return true;
    }

    @ApiOperation(value = "更新用例信息")
    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    public boolean updateCaseInfo(@RequestBody CaseInfoDto caseInfoDto) {
        caseService.updateCaseInfo(caseInfoDto);
        return true;
    }

    @ApiOperation(value = "删除用例信息")
    @RequestMapping(value = "/info/{group_id}", method = RequestMethod.DELETE)
    public boolean deleteCaseInfo(@PathVariable(value = "group_id") String groupId, @RequestParam(value = "case_id") String caseId) {
        caseService.deleteCaseInfo(groupId, caseId);
        return true;
    }

}
