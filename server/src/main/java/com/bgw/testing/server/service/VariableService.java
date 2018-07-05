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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${default.environment.id}")
    private String environmentId;

    /**
     * 变量初始化
     */
    public void initVariable() {
        //初始化全局变量
        Map<String, Object> params = new HashMap<>();
        params.put("type", VariableType.GLOBAL.type);
        VariableContext.getInstance().initVariable(tsVariableListMapper.selectVariableList(params));
        //初始化环境变量
        initEnvironmentVariable(this.environmentId);
    }

    /**
     * 初始化环境变量
     * @param environmentId
     */
    public void initEnvironmentVariable(String environmentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", VariableType.ENVIRONMENT.type);
        params.put("environmentId", environmentId);
        VariableContext.getInstance().initVariable(tsVariableListMapper.selectVariableList(params));
    }

    /**
     * 分页查询环境变量
     * @param type 变量类型
     * @param environmentId 环境ID
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @param var4 模糊查询字符串
     * @return
     */
    public PageInfo<VariableInfoDto> getVariableList(String type, String environmentId, Integer pageNum, Integer pageSize, String var4) {
        List<TsVariableList> tsVariableLists = VariableContext.getInstance().getAllVariableList();

        //根据变量类型过滤
        tsVariableLists = tsVariableLists.parallelStream()
                .filter(tsVariableList -> tsVariableList.getType().equals(type))
                .collect(Collectors.toList());

        //根据环境ID过滤
        if (type.equals(VariableType.ENVIRONMENT.type) && StringUtils.isNotBlank(environmentId)) {
            tsVariableLists = tsVariableLists.parallelStream()
                    .filter(tsVariableList -> tsVariableList.getEnvironmentId().equals(environmentId))
                    .collect(Collectors.toList());
        }

        //模糊查询
        if (StringUtils.isNotBlank(var4)) {
            tsVariableLists = tsVariableLists.parallelStream()
                    .filter(tsVariableList -> tsVariableList.getConfigKey().contains(var4)
                            || tsVariableList.getConfigValue().contains(var4)
                            || tsVariableList.getRemark().contains(var4))
                    .collect(Collectors.toList());
        }

        List<VariableInfoDto> variableInfoDtos = tsVariableLists
                .parallelStream()
                .map(this::convertToVariableInfoDto)
                .collect(Collectors.toList());

        return new PageInfo<>(variableInfoDtos, pageNum, pageSize);
    }

    /**
     * 增加变量
     * @param variableInfoDto
     */
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

    /**
     * 更新变量
     * @param variableInfoDto
     */
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

    /**
     * 删除变量
     * @param id
     */
    public void delVariable(String id) {
        tsVariableListMapper.deleteByPrimaryKey(id);
        VariableContext variableContext = VariableContext.getInstance();
        List<TsVariableList> tsVariableLists = variableContext.getAllVariableList().stream()
                .filter(tsVariableList -> tsVariableList.getId().equals(id)).collect(Collectors.toList());
        if (tsVariableLists != null && tsVariableLists.size() == 1) {
            variableContext.delVariable(tsVariableLists.get(0));
        }
    }

    /**
     * 变量是否存在
     * @param variableInfoDto
     * @return
     */
    private boolean isExist(VariableInfoDto variableInfoDto) {
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

    private VariableInfoDto convertToVariableInfoDto(TsVariableList tsVariableList) {
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
