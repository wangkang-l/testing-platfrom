package com.bgw.testing.server.service;

import com.bgw.testing.common.ErrorCode;
import com.bgw.testing.common.dto.CaseBasicDto;
import com.bgw.testing.common.dto.CaseContentDto;
import com.bgw.testing.common.dto.StepDto;
import com.bgw.testing.common.enums.StepTypeEnum;
import com.bgw.testing.common.enums.VariableTypeEnum;
import com.bgw.testing.dao.bgw_automation.mapper.*;
import com.bgw.testing.dao.bgw_automation.pojo.TsCaseBasicInfo;
import com.bgw.testing.dao.bgw_automation.pojo.TsStepBasicInfo;
import com.bgw.testing.dao.bgw_automation.pojo.TsVariableList;
import com.bgw.testing.server.config.ServerException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CaseService {

    @Autowired
    private TsCaseBasicInfoMapper tsCaseBasicInfoMapper;
    @Autowired
    private TsStepBasicInfoMapper tsStepBasicInfoMapper;
    @Autowired
    private TsRedisInfoMapper tsRedisInfoMapper;
    @Autowired
    private TsMysqlInfoMapper tsMysqlInfoMapper;
    @Autowired
    private VariableService variableService;

    public PageInfo<TsCaseBasicInfo> getCaseInfo(String groupId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "create_time desc");
        return new PageInfo<TsCaseBasicInfo>(tsCaseBasicInfoMapper.selectByGroupId(groupId));
    }


    public PageInfo<TsCaseBasicInfo> getAllCaseInfo(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "create_time desc");
        return new PageInfo<TsCaseBasicInfo>(tsCaseBasicInfoMapper.selectAll());
    }

    private CaseContentDto getCaseContent(String caseId) {
        CaseContentDto caseContentDto = new CaseContentDto();
        List<TsStepBasicInfo> tsStepBasicInfos = tsStepBasicInfoMapper.selectByCaseId(caseId);
        List<StepDto> stepDtos = new ArrayList<>();
        tsStepBasicInfos.parallelStream().forEach(tsStepBasicInfo -> {
            StepDto stepDto = new StepDto();
            if (tsStepBasicInfo.getStepType().equals(StepTypeEnum.BASIC.key)) {
                caseContentDto.setCondition(tsStepBasicInfo.getCondition());
                Map<String, String> variableList = new HashMap<>();
                variableService.getVariableList(caseId, VariableTypeEnum.CASE.key).forEach(tsVariableList -> {
                    variableList.put(tsVariableList.getConfigKey(), tsVariableList.getConfigValue());
                });
                caseContentDto.setInitCaseVariables(variableList);
            } else if (tsStepBasicInfo.getStepType().equals(StepTypeEnum.API.key)) {

            } else if (tsStepBasicInfo.getStepType().equals(StepTypeEnum.SQL.key)) {

            } else if (tsStepBasicInfo.getStepType().equals(StepTypeEnum.REDIS.key)) {

            } else if (tsStepBasicInfo.getStepType().equals(StepTypeEnum.FUNC.key)) {

            } else {
                throw ServerException.fromKey(ErrorCode.UNKNOWN_STEP_TYPE.errorKey, ErrorCode.UNKNOWN_STEP_TYPE.description);
            }
        });
        return caseContentDto;
    }

    private StepDto stepBasicInfoConvertToDto(TsStepBasicInfo tsStepBasicInfo) {
        StepDto stepDto = new StepDto();
        stepDto.setStepId(tsStepBasicInfo.getId());
        stepDto.setDescription(tsStepBasicInfo.getDescription());
        stepDto.setCondition(tsStepBasicInfo.getCondition());
        stepDto.setApiRequest(tsStepBasicInfo.getInterfaceId());
        //TODO 待完善
        stepDto.setExtractor(tsStepBasicInfo.getExtractor());
        stepDto.setElement(tsStepBasicInfo.getElement());
        stepDto.setVerifier(tsStepBasicInfo.getVerifier());
        return stepDto;
    }


    private CaseBasicDto caseBasicInfoConverTotDto(TsCaseBasicInfo tsCaseBasicInfo) {
        CaseBasicDto caseBasicDto = new CaseBasicDto();
        caseBasicDto.setCaseId(tsCaseBasicInfo.getId());
        caseBasicDto.setGroupId(tsCaseBasicInfo.getGroupId());
        caseBasicDto.setCaseName(tsCaseBasicInfo.getCaseName());
        caseBasicDto.setDescription(tsCaseBasicInfo.getDescription());
        caseBasicDto.setPriority(tsCaseBasicInfo.getPriority());
        caseBasicDto.setInvalid(tsCaseBasicInfo.getIsInvalid());
        caseBasicDto.setCreateTime(tsCaseBasicInfo.getCreateTime());
        caseBasicDto.setCreateBy(tsCaseBasicInfo.getCreateBy());
        caseBasicDto.setUpdateTime(tsCaseBasicInfo.getUpdateTime());
        caseBasicDto.setUpdateBy(tsCaseBasicInfo.getUpdateBy());

        return caseBasicDto;
    }


}
