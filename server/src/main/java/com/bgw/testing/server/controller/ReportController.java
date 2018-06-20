package com.bgw.testing.server.controller;

import com.bgw.testing.common.AppConst;
import com.bgw.testing.common.dto.PageInfo;
import com.bgw.testing.common.dto.CaseReportDto;
import com.bgw.testing.common.dto.ReportInfoDto;
import com.bgw.testing.common.dto.StepReportDto;
import com.bgw.testing.server.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Api(description = "报告管理")
@RequestMapping(value = AppConst.BASE_PATH + "report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "获取报告结果")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public PageInfo<ReportInfoDto> getReportResult(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return reportService.getReportResult(pageNum,pageSize);
    }

    @ApiOperation(value = "获取用例结果")
    @RequestMapping(value = "/caseInfo", method = RequestMethod.GET)
    public PageInfo<CaseReportDto> getReportCaseResult(@RequestParam String batchNo, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return reportService.getReportCaseResult(batchNo,pageNum,pageSize);
    }

    @ApiOperation(value = "获取步骤结果")
    @RequestMapping(value = "/stepInfo", method = RequestMethod.GET)
    public PageInfo<StepReportDto> getReportStepResult(@RequestParam String caseResultId, @RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return reportService.getReportStepResult(caseResultId,pageNum,pageSize);
    }
}
