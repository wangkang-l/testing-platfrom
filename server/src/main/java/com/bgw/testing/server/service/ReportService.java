package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.PageInfo;
import com.bgw.testing.common.dto.CaseReportDto;
import com.bgw.testing.common.dto.ReportInfoDto;
import com.bgw.testing.common.dto.StepReportDto;
import com.bgw.testing.dao.mapper.bgw_automation.TsReportCaseResultMapper;
import com.bgw.testing.dao.mapper.bgw_automation.TsReportResultMapper;
import com.bgw.testing.dao.mapper.bgw_automation.TsReportStepResultMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsReportCaseResult;
import com.bgw.testing.dao.pojo.bgw_automation.TsReportResult;
import com.bgw.testing.dao.pojo.bgw_automation.TsReportStepResult;
import com.bgw.testing.server.util.BaseStringUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private TsReportResultMapper tsReportResultMapper;
    @Autowired
    private TsReportCaseResultMapper tsReportCaseResultMapper;
    @Autowired
    private TsReportStepResultMapper tsReportStepResultMapper;

    /**
     * 生成测试报告，并保存
     * @param caseReportMap
     * @param taskId
     * @return
     */
    public ReportInfoDto initReportInfo(Map<String, List<CaseReportDto>> caseReportMap, String taskId) {
        ReportInfoDto reportInfoDto = new ReportInfoDto();
        reportInfoDto.setBatchNo(BaseStringUtils.uuidSimple());
        reportInfoDto.setTaskId(taskId);
        caseReportMap.values().forEach(caseReportDtos -> {
            reportInfoDto.setTotalNum(reportInfoDto.getTotalNum() + caseReportDtos.size());
            reportInfoDto.getCaseReportDtoList().addAll(caseReportDtos);
            caseReportDtos.forEach(caseReportDto -> {
                //设置开始时间
                if (caseReportDto.getStartTime() != null && caseReportDto.getStartTime().before(reportInfoDto.getStartTime())) {
                    reportInfoDto.setStartTime(caseReportDto.getStartTime());
                }
                //设置结束时间
                if (caseReportDto.getEndTime() != null && caseReportDto.getEndTime().after(reportInfoDto.getEndTime())) {
                    reportInfoDto.setEndTime(caseReportDto.getEndTime());
                }
                //设置成功与失败数量
                if (caseReportDto.getResult()) {
                    reportInfoDto.setSuccNum(reportInfoDto.getSuccNum() + 1);
                } else {
                    reportInfoDto.setFailNum(reportInfoDto.getFailNum() + 1);
                }
            });
        });
        reportInfoDto.passRate();
        //保存测试报告
        return reportInfoDto;
    }

    /**
     * 分页查询报告结果
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<ReportInfoDto> getReportResult(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize,"create_time desc");
        List<TsReportResult> tsReportResults = tsReportResultMapper.selectAll();
        List<ReportInfoDto> reportInfoDtos = new ArrayList<>();
        if (tsReportResults != null && tsReportResults.size() > 0) {
            tsReportResults.forEach(tsReportResult -> {
                reportInfoDtos.add(convertToReportResultDto(tsReportResult));
            });
        }
        return new PageInfo<>(reportInfoDtos);
    }

    /**
     * 根据批次号查询报告结果
     * @param batchNo
     * @return
     */
    public ReportInfoDto getReportResultByBatchNo(String batchNo){
        TsReportResult tsReportResult = tsReportResultMapper.selectByPrimaryKey(batchNo);
        return convertToReportResultDto(tsReportResult);
    }

    /**
     * 分页查询用例结果
     * @param batchNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<CaseReportDto> getReportCaseResult(String batchNo, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize,"create_time desc");
        List<TsReportCaseResult> tsReportCaseResults = tsReportCaseResultMapper.selectByBatchNo(batchNo);
        List<CaseReportDto> caseReportDtos = new ArrayList<>();
        if (tsReportCaseResults != null && tsReportCaseResults.size() > 0) {
            tsReportCaseResults.forEach(tsReportCaseResult -> {
                caseReportDtos.add(convertToReportCaseResultDto(tsReportCaseResult));
            });
        }
        return new PageInfo<>(caseReportDtos);
    }

    /**
     * 分页查询步骤结果
     * @param caseResultId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<StepReportDto> getReportStepResult(String caseResultId, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize,"create_time desc");
        List<TsReportStepResult> tsReportStepResults = tsReportStepResultMapper.selectByCaseResultId(caseResultId);
        List<StepReportDto> stepReportDtos = new ArrayList<>();
        if (tsReportStepResults != null && tsReportStepResults.size() > 0) {
            tsReportStepResults.forEach(tsReportStepResult -> {
                stepReportDtos.add(convertToReportStepResultDto(tsReportStepResult));
            });
        }
        return new PageInfo<>(stepReportDtos);
    }

    /**
     * 新增报告结果
     * @param reportInfoDto
     */
    @Transactional
    public void addReportResult(ReportInfoDto reportInfoDto) {
        TsReportResult tsReportResult = convertToTsReportResult(reportInfoDto);
        tsReportResult.setBatchNo(BaseStringUtils.uuidSimple());
        tsReportResult.setCreateTime(new Date());
        tsReportResult.setExecutor("System");
        tsReportResultMapper.insertSelective(tsReportResult);
        if(reportInfoDto.getCaseReportDtoList() != null && reportInfoDto.getCaseReportDtoList().size() > 0){
            reportInfoDto.getCaseReportDtoList().forEach(ReportCaseResultDto ->
                    addReportCaseResult(tsReportResult.getBatchNo(), ReportCaseResultDto)
            );
        }
    }

    /**
     * 新增用例结果
     * @param caseReportDto
     * @param batchNo
     */
    private void addReportCaseResult(String batchNo, CaseReportDto caseReportDto) {
        TsReportCaseResult tsReportCaseResult = convertToTsReportCaseResult(caseReportDto);
        tsReportCaseResult.setId(BaseStringUtils.uuidSimple());
        tsReportCaseResult.setBatchNo(batchNo);
        tsReportCaseResult.setCreateTime(new Date());
        tsReportCaseResultMapper.insertSelective(tsReportCaseResult);
        if(caseReportDto.getStepReportDtoList() != null && caseReportDto.getStepReportDtoList().size() > 0){
            caseReportDto.getStepReportDtoList().forEach(StepReportDto ->
                    addReportStepResult(tsReportCaseResult.getId(), StepReportDto)
            );
        }

    }

    /**
     * 新增步骤结果
     * @param stepReportDto
     * @param caseResultId
     */
    private void addReportStepResult(String caseResultId,StepReportDto stepReportDto) {
        TsReportStepResult tsReportStepResult = convertToTsReportStepResult(stepReportDto);
        tsReportStepResult.setId(BaseStringUtils.uuidSimple());
        tsReportStepResult.setCaseResultId(caseResultId);
        tsReportStepResult.setCreateTime(new Date());
        tsReportStepResultMapper.insertSelective(tsReportStepResult);
    }

    private TsReportResult convertToTsReportResult(ReportInfoDto reportInfoDto){
        TsReportResult tsReportResult = new TsReportResult();
        tsReportResult.setBatchNo(reportInfoDto.getBatchNo());
        tsReportResult.setTaskId(reportInfoDto.getTaskId());
        tsReportResult.setStartTime(reportInfoDto.getStartTime());
        tsReportResult.setEndTime(reportInfoDto.getEndTime());
        tsReportResult.setTotalNum(reportInfoDto.getTotalNum());
        tsReportResult.setSuccNum(reportInfoDto.getSuccNum());
        tsReportResult.setFailNum(reportInfoDto.getFailNum());
        tsReportResult.setPassRate(reportInfoDto.getPassRate());
        tsReportResult.setExecutor(reportInfoDto.getExecutor());
        return tsReportResult;
    }

    private ReportInfoDto convertToReportResultDto(TsReportResult tsReportResult){
        ReportInfoDto reportInfoDto = new ReportInfoDto();
        reportInfoDto.setBatchNo(tsReportResult.getBatchNo());
        reportInfoDto.setTaskId(tsReportResult.getTaskId());
        reportInfoDto.setStartTime(tsReportResult.getStartTime());
        reportInfoDto.setEndTime(tsReportResult.getEndTime());
        reportInfoDto.setTotalNum(tsReportResult.getTotalNum());
        reportInfoDto.setSuccNum(tsReportResult.getSuccNum());
        reportInfoDto.setFailNum(tsReportResult.getFailNum());
        reportInfoDto.setPassRate(tsReportResult.getPassRate());
        reportInfoDto.setExecutor(tsReportResult.getExecutor());
        return reportInfoDto;
    }

    private TsReportCaseResult convertToTsReportCaseResult(CaseReportDto caseReportDto) {
        TsReportCaseResult tsReportCaseResult = new TsReportCaseResult();
        tsReportCaseResult.setId(caseReportDto.getCaseResultId());
        tsReportCaseResult.setCaseId(caseReportDto.getCaseId());
        tsReportCaseResult.setCaseName(caseReportDto.getCaseName());
        tsReportCaseResult.setTraceLogId(caseReportDto.getTraceLogId());
        tsReportCaseResult.setResult(caseReportDto.getResult());
        tsReportCaseResult.setStartTime(caseReportDto.getStartTime());
        tsReportCaseResult.setEndTime(caseReportDto.getEndTime());
        return tsReportCaseResult;
    }

    private CaseReportDto convertToReportCaseResultDto(TsReportCaseResult tsReportCaseResult ) {
        CaseReportDto caseReportDto = new CaseReportDto();
        caseReportDto.setCaseResultId(tsReportCaseResult.getId());
        caseReportDto.setCaseId(tsReportCaseResult.getCaseId());
        caseReportDto.setCaseName(tsReportCaseResult.getCaseName());
        caseReportDto.setTraceLogId(tsReportCaseResult.getTraceLogId());
        caseReportDto.setResult(tsReportCaseResult.getResult());
        caseReportDto.setStartTime(tsReportCaseResult.getStartTime());
        caseReportDto.setEndTime(tsReportCaseResult.getEndTime());
        return caseReportDto;
    }

    private TsReportStepResult convertToTsReportStepResult(StepReportDto stepReportDto) {
        TsReportStepResult tsReportStepResult = new TsReportStepResult();
        tsReportStepResult.setId(stepReportDto.getStepResultId());
        tsReportStepResult.setDescription(stepReportDto.getDescription());
        tsReportStepResult.setStepType(stepReportDto.getStepType());
        tsReportStepResult.setResult(stepReportDto.getResult());
        tsReportStepResult.setStartTime(stepReportDto.getStartTime());
        tsReportStepResult.setEndTime(stepReportDto.getEndTime());
        tsReportStepResult.setStepContent(stepReportDto.getStepContent());
        tsReportStepResult.setRequestContent(stepReportDto.getRequestContent());
        tsReportStepResult.setResponseContent(stepReportDto.getResponseContent());
        tsReportStepResult.setErrorInfo(stepReportDto.getErrorInfo());
        return tsReportStepResult;
    }

    private StepReportDto convertToReportStepResultDto(TsReportStepResult tsReportStepResult ) {
        StepReportDto stepReportDto = new StepReportDto();
        stepReportDto.setStepResultId(tsReportStepResult.getId());
        stepReportDto.setDescription(tsReportStepResult.getDescription());
        stepReportDto.setStepType(tsReportStepResult.getStepType());
        stepReportDto.setResult(tsReportStepResult.getResult());
        stepReportDto.setStartTime(tsReportStepResult.getStartTime());
        stepReportDto.setEndTime(tsReportStepResult.getEndTime());
        stepReportDto.setStepContent(tsReportStepResult.getStepContent());
        stepReportDto.setRequestContent(tsReportStepResult.getRequestContent());
        stepReportDto.setResponseContent(tsReportStepResult.getResponseContent());
        stepReportDto.setErrorInfo(tsReportStepResult.getErrorInfo());
        return stepReportDto;
    }
}
