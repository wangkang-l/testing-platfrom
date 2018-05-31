package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.PageInfo;
import com.bgw.testing.common.dto.VariableInfoDto;
import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.common.enums.VariableType;
import com.bgw.testing.dao.mapper.bgw_automation.TsVariableListMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsVariableList;
import com.bgw.testing.server.VariableContext;
import com.bgw.testing.server.config.ServerException;
import com.bgw.testing.server.util.BaseStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VariableService {

    @Autowired
    private TsVariableListMapper tsVariableListMapper;

    @PostConstruct
    public void initVariable() {
        VariableContext.getInstance().initVariable(tsVariableListMapper.selectAll());
    }

    public PageInfo<VariableInfoDto> getVariableList(String type, String environmentId, Integer pageNum, Integer pageSize, String var4) {
        List<TsVariableList> tsVariableLists = VariableContext.getInstance().getAllVariableList();

        //根据变量类型过滤
        List<VariableInfoDto> variableInfoDtos = tsVariableLists.parallelStream()
                .filter(tsVariableList -> tsVariableList.getType().equals(type))
                .map(VariableService::convertToVariableInfoDto)
                .collect(Collectors.toList());

        //更具环境ID过滤
        if (type.equals(VariableType.ENVIRONMENT.type) && StringUtils.isNotBlank(environmentId)) {
            variableInfoDtos = variableInfoDtos.parallelStream()
                    .filter(variableInfoDto -> variableInfoDto.getEnvironmentId().equals(environmentId))
                    .collect(Collectors.toList());
        }

        //模糊查询
        if (StringUtils.isNotBlank(var4)) {
            variableInfoDtos = variableInfoDtos.parallelStream()
                    .filter(variableInfoDto -> variableInfoDto.getConfigKey().contains(var4)
                            || variableInfoDto.getConfigValue().contains(var4)
                            || variableInfoDto.getRemark().contains(var4))
                    .collect(Collectors.toList());
        }

        return new PageInfo<>(variableInfoDtos, pageNum, pageSize);
    }

    public void addVariable(VariableInfoDto variableInfoDto) {
        if (isExist(variableInfoDto)) {
            throw new ServerException(ErrorCode.ALREADY_EXISTS, variableInfoDto.getConfigKey());
        }
        TsVariableList tsVariableList = convertToTsVariableList(variableInfoDto);
        tsVariableList.setId(BaseStringUtils.uuidSimple());
        tsVariableList.setCreateTime(new Date());
        tsVariableList.setCreateBy("System");
        tsVariableListMapper.insertSelective(tsVariableList);
        VariableContext.getInstance().addOrUpdateVariable(tsVariableList, false);
    }

    public void updateVariable(VariableInfoDto variableInfoDto) {
        if (!isExist(variableInfoDto)) {
            throw new ServerException(ErrorCode.NOT_EXIST, variableInfoDto.getConfigKey());
        }
        TsVariableList tsVariableList = convertToTsVariableList(variableInfoDto);
        tsVariableList.setUpdateTime(new Date());
        tsVariableList.setUpdateBy("System");
        tsVariableListMapper.updateByPrimaryKeySelective(tsVariableList);
        VariableContext.getInstance().addOrUpdateVariable(tsVariableList, true);
    }

    public void delVariable(String id) {
        tsVariableListMapper.deleteByPrimaryKey(id);
        VariableContext variableContext = VariableContext.getInstance();
        List<TsVariableList> tsVariableLists = variableContext.getAllVariableList().stream()
                .filter(tsVariableList -> tsVariableList.getId().equals(id)).collect(Collectors.toList());
        if (tsVariableLists != null && tsVariableLists.size() == 1) {
            variableContext.delVariable(tsVariableLists.get(0));
        }
    }

    public boolean isExist(VariableInfoDto variableInfoDto) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", variableInfoDto.getType());
        params.put("environmentId", variableInfoDto.getEnvironmentId());
        params.put("configKey", variableInfoDto.getConfigKey());
        List<TsVariableList> tsVariableLists = tsVariableListMapper.selectVariableList(params);
        if (tsVariableLists == null || tsVariableLists.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private TsVariableList convertToTsVariableList(VariableInfoDto variableInfoDto) {
        TsVariableList tsVariableList = new TsVariableList();
        tsVariableList.setId(variableInfoDto.getVariableId());
        tsVariableList.setType(variableInfoDto.getType());
        tsVariableList.setEnvironmentId(variableInfoDto.getEnvironmentId());
        tsVariableList.setConfigKey(variableInfoDto.getConfigKey());
        tsVariableList.setConfigValue(variableInfoDto.getConfigValue());
        tsVariableList.setRemark(variableInfoDto.getRemark());
        return tsVariableList;
    }

    private static VariableInfoDto convertToVariableInfoDto(TsVariableList tsVariableList) {
        VariableInfoDto variableInfoDto = new VariableInfoDto();
        variableInfoDto.setVariableId(tsVariableList.getId());
        variableInfoDto.setType(tsVariableList.getType());
        variableInfoDto.setEnvironmentId(tsVariableList.getEnvironmentId());
        variableInfoDto.setConfigKey(tsVariableList.getConfigKey());
        variableInfoDto.setConfigValue(tsVariableList.getConfigValue());
        variableInfoDto.setRemark(tsVariableList.getRemark());
        return variableInfoDto;
    }

}
