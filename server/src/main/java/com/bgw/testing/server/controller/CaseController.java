package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.dao.bgw_automation.pojo.TsCaseBasicInfo;
import com.bgw.testing.server.service.CaseService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Api(description = AppConst.SERVICE_NAME_CN)
@RequestMapping(value = AppConst.BASE_PATH + "case")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @ApiOperation(value = "根据组ID获取用例信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public PageInfo<TsCaseBasicInfo> getCaseInfo(@RequestParam String groupId, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return caseService.getCaseInfo(groupId, pageNum, pageSize);
    }

}
