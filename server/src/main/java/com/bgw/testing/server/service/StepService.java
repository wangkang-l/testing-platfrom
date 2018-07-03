package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.*;
import com.bgw.testing.dao.mapper.bgw_automation.TsStepInfoMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsStepInfo;
import com.bgw.testing.server.util.BaseJsonUtils;
import com.bgw.testing.server.util.BaseStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StepService {

    @Autowired
    private TsStepInfoMapper tsStepInfoMapper;

    public List<StepDto> getStepInfo(String caseId) {
        return tsStepInfoMapper.selectByCaseId(caseId)
                .stream()
                .map(this::convertToStepDto)
                .collect(Collectors.toList());
    }

    public void addStepInfo(StepDto stepDto) {
        TsStepInfo tsStepInfo = convertToTsStepInfo(stepDto);
        tsStepInfo.setId(BaseStringUtils.uuidSimple());
        tsStepInfo.setCreateTime(new Date());
        tsStepInfo.setCreateBy("System");
        tsStepInfoMapper.insertSelective(tsStepInfo);
    }

    public void updateStepInfo(StepDto stepDto) {
        TsStepInfo tsStepInfo = convertToTsStepInfo(stepDto);
        tsStepInfo.setUpdateTime(new Date());
        tsStepInfo.setUpdateBy("System");
        tsStepInfoMapper.updateByPrimaryKeySelective(tsStepInfo);
    }

    public void delStepInfoByStepId(String stepId) {
        tsStepInfoMapper.deleteByPrimaryKey(stepId);
    }

    public void delStepInfoByCaseId(String caseId) {
        tsStepInfoMapper.deleteByCaseId(caseId);
    }

    private StepDto convertToStepDto(TsStepInfo tsStepInfo) {
        StepDto stepDto = new StepDto();
        stepDto.setStepId(tsStepInfo.getId());
        stepDto.setCaseId(tsStepInfo.getCaseId());
        stepDto.setDescription(tsStepInfo.getDescription());
        stepDto.setPrecondition(tsStepInfo.getPrecondition());
        stepDto.setStepType(tsStepInfo.getType());
        stepDto.setPriority(tsStepInfo.getPriority());
        stepDto.setInitTemporaryVariables(BaseJsonUtils.readValue(tsStepInfo.getInitTemporaryVariables(), Map.class));
        stepDto.setFunc(tsStepInfo.getFunc());
        stepDto.setHttpRequest(BaseJsonUtils.readValue(tsStepInfo.getHttpRequest(), HttpRequestDto.class));
        stepDto.setTemplateId(tsStepInfo.getTemplateId());
        stepDto.setRedisInfo(BaseJsonUtils.readValue(tsStepInfo.getRedisRequest(), RedisInfoDto.class));
        stepDto.setMysqlInfo(BaseJsonUtils.readValue(tsStepInfo.getMysqlRequest(), MySqlInfoDto.class));
        stepDto.setKeyInGlobalVariable(tsStepInfo.getKeyInGlobalVariable());
        stepDto.setKeyInEnvironmentVariable(tsStepInfo.getKeyInEnvironmentVariable());
        stepDto.setKeyInTemporaryVariable(tsStepInfo.getKeyInTemporaryVariable());
        stepDto.setElement(tsStepInfo.getElement());
        stepDto.setExtractor(BaseJsonUtils.readValue(tsStepInfo.getExtractor(), ExtractorDto.class));
        if (StringUtils.isNotBlank(tsStepInfo.getVerifier())) {
            stepDto.setVerifiers(BaseJsonUtils.readValues(tsStepInfo.getVerifier(), VerifierDto.class));
        }
        return stepDto;
    }

    private TsStepInfo convertToTsStepInfo(StepDto stepDto) {
        TsStepInfo tsStepInfo = new TsStepInfo();
        tsStepInfo.setId(stepDto.getStepId());
        tsStepInfo.setCaseId(stepDto.getCaseId());
        tsStepInfo.setDescription(stepDto.getDescription());
        tsStepInfo.setPrecondition(stepDto.getPrecondition());
        tsStepInfo.setType(stepDto.getStepType());
        tsStepInfo.setPriority(stepDto.getPriority());
        tsStepInfo.setInitTemporaryVariables(BaseJsonUtils.writeValue(stepDto.getInitTemporaryVariables()));
        tsStepInfo.setFunc(stepDto.getFunc());
        tsStepInfo.setHttpRequest(BaseJsonUtils.writeValue(stepDto.getHttpRequest()));
        tsStepInfo.setTemplateId(stepDto.getTemplateId());
        tsStepInfo.setRedisRequest(BaseJsonUtils.writeValue(stepDto.getRedisInfo()));
        tsStepInfo.setMysqlRequest(BaseJsonUtils.writeValue(stepDto.getMysqlInfo()));
        tsStepInfo.setKeyInGlobalVariable(stepDto.getKeyInGlobalVariable());
        tsStepInfo.setKeyInEnvironmentVariable(stepDto.getKeyInEnvironmentVariable());
        tsStepInfo.setKeyInTemporaryVariable(stepDto.getKeyInTemporaryVariable());
        tsStepInfo.setElement(stepDto.getElement());
        tsStepInfo.setExtractor(BaseJsonUtils.writeValue(stepDto.getExtractor()));
        tsStepInfo.setVerifier(BaseJsonUtils.writeValue(stepDto.getVerifiers()));
        return tsStepInfo;
    }

}
