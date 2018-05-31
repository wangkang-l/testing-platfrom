package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.InterfaceInfoDto;
import com.bgw.testing.common.dto.InterfaceParamDto;
import com.bgw.testing.dao.mapper.bgw_automation.TsInterfaceBasicInfoMapper;
import com.bgw.testing.dao.mapper.bgw_automation.TsInterfaceParameterMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsInterfaceBasicInfo;
import com.bgw.testing.dao.pojo.bgw_automation.TsInterfaceParameter;
import com.bgw.testing.server.util.BaseStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InterfaceService {
    @Autowired
    private TsInterfaceBasicInfoMapper tsInterfaceBasicInfoMapper;
    @Autowired
    private TsInterfaceParameterMapper tsInterfaceParameterMapper;

    /**
     * 根据接口描述模糊查询接口
     * @param description
     */
    public List<InterfaceInfoDto> getInterfaceInfoByDescription(String description) {
        List<TsInterfaceBasicInfo> tsInterfaceBasicInfos = tsInterfaceBasicInfoMapper.selectByDescription(description);
        List<InterfaceInfoDto> interfaceInfoDtos = new ArrayList<>();
        if (tsInterfaceBasicInfos != null && tsInterfaceBasicInfos.size() > 0) {
            for(int loop=0;loop<tsInterfaceBasicInfos.size();loop++) {
                List<TsInterfaceParameter> tsInterfaceParameters = tsInterfaceParameterMapper.selectByInterfaceId(tsInterfaceBasicInfos.get(loop).getId());
                List<InterfaceParamDto> interfaceParamDtos = new ArrayList<>();
                if(tsInterfaceParameters != null && tsInterfaceParameters.size() > 0) {
                    tsInterfaceParameters.forEach(tsInterfaceParameter -> {
                        interfaceParamDtos.add(interfaceParamDbConvertDto(tsInterfaceParameter));
                    });
                }
                interfaceInfoDtos.add(interfaceInfoDbConvertDto(tsInterfaceBasicInfos.get(loop)));
                interfaceInfoDtos.get(loop).setInterfaceParamDtoList(interfaceParamDtos);
            }
        }
        return interfaceInfoDtos;
    }

    /**
     * 新增接口信息
     * @param interfaceInfoDto
     */
    public void addInterfaceInfo(InterfaceInfoDto interfaceInfoDto) {
        interfaceInfoDto.setInterfaceId(BaseStringUtils.uuidSimple());
        interfaceInfoDto.setCreateTime(new Date());
        interfaceInfoDto.setCreateBy("System");
        interfaceInfoDto.setUpdateTime(new Date());
        interfaceInfoDto.setUpdateBy("System");
        tsInterfaceBasicInfoMapper.insertSelective(interfaceInfoDtoConvertDb(interfaceInfoDto));
        if(interfaceInfoDto.getInterfaceParamDtoList().size()>0){
            processInterfaceParam(interfaceInfoDto);
        }
    }

    /**
     * 删除接口信息和参数表
     * @param interfaceId
     */
    public void deleteInterfaceInfo(String interfaceId) {
        tsInterfaceBasicInfoMapper.deleteByPrimaryKey(interfaceId);
        tsInterfaceParameterMapper.deleteByInterfaceId(interfaceId);
    }

    /**
     * 更新接口信息
     * @param interfaceInfoDto
     */
    public void updateInterfaceInfo(InterfaceInfoDto interfaceInfoDto) {
        interfaceInfoDto.setUpdateTime(new Date());
        interfaceInfoDto.setUpdateBy("System");
        tsInterfaceBasicInfoMapper.updateByPrimaryKeySelective(interfaceInfoDtoConvertDb(interfaceInfoDto));
        tsInterfaceParameterMapper.deleteByInterfaceId(interfaceInfoDto.getInterfaceId());
        if(interfaceInfoDto.getInterfaceParamDtoList().size()>0){
            processInterfaceParam(interfaceInfoDto);
        }
    }

    /**
     * 新增接口参数
     * @param interfaceParamDto
     */
    private void addInterfaceParam(InterfaceParamDto interfaceParamDto) {
        interfaceParamDto.setInterfaceParamId(BaseStringUtils.uuidSimple());
        interfaceParamDto.setCreateTime(new Date());
        interfaceParamDto.setCreateBy("System");
        interfaceParamDto.setUpdateTime(new Date());
        interfaceParamDto.setUpdateBy("System");
        tsInterfaceParameterMapper.insertSelective(interfaceParamDtoConvertDb(interfaceParamDto));
    }

    /**
     * 接口参数处理
     * @param interfaceInfoDto
     */
    private void processInterfaceParam(InterfaceInfoDto interfaceInfoDto){
        for(int loop=0;loop<interfaceInfoDto.getInterfaceParamDtoList().size();loop++){
            InterfaceParamDto interfaceParamDto = interfaceInfoDto.getInterfaceParamDtoList().get(loop);
            interfaceParamDto.setInterfaceId(interfaceInfoDto.getInterfaceId());
            addInterfaceParam(interfaceParamDto);
        }
    }

    private InterfaceInfoDto interfaceInfoDbConvertDto(TsInterfaceBasicInfo tsInterfaceBasicInfo){
        InterfaceInfoDto interfaceInfoDto = new InterfaceInfoDto();
        interfaceInfoDto.setInterfaceId(tsInterfaceBasicInfo.getId());
        interfaceInfoDto.setDescription(tsInterfaceBasicInfo.getDescription());
        interfaceInfoDto.setPath(tsInterfaceBasicInfo.getPath());
        interfaceInfoDto.setMethod(tsInterfaceBasicInfo.getMethod());
        interfaceInfoDto.setBody(tsInterfaceBasicInfo.getBody());
        interfaceInfoDto.setCreateTime(tsInterfaceBasicInfo.getCreateTime());
        interfaceInfoDto.setCreateBy(tsInterfaceBasicInfo.getCreateBy());
        interfaceInfoDto.setUpdateTime(tsInterfaceBasicInfo.getUpdateTime());
        interfaceInfoDto.setUpdateBy(tsInterfaceBasicInfo.getUpdateBy());
        return interfaceInfoDto;
    }

    private TsInterfaceBasicInfo interfaceInfoDtoConvertDb(InterfaceInfoDto interfaceInfoDto){
        TsInterfaceBasicInfo tsInterfaceBasicInfo = new TsInterfaceBasicInfo();
        tsInterfaceBasicInfo.setId(interfaceInfoDto.getInterfaceId());
        tsInterfaceBasicInfo.setDescription(interfaceInfoDto.getDescription());
        tsInterfaceBasicInfo.setPath(interfaceInfoDto.getPath());
        tsInterfaceBasicInfo.setMethod(interfaceInfoDto.getMethod());
        tsInterfaceBasicInfo.setBody(interfaceInfoDto.getBody());
        tsInterfaceBasicInfo.setCreateTime(interfaceInfoDto.getCreateTime());
        tsInterfaceBasicInfo.setCreateBy(interfaceInfoDto.getCreateBy());
        tsInterfaceBasicInfo.setUpdateTime(interfaceInfoDto.getUpdateTime());
        tsInterfaceBasicInfo.setUpdateBy(interfaceInfoDto.getUpdateBy());
        return tsInterfaceBasicInfo;
    }

    private InterfaceParamDto interfaceParamDbConvertDto(TsInterfaceParameter tsInterfaceParameter){
        InterfaceParamDto interfaceParamDto = new InterfaceParamDto();
        interfaceParamDto.setInterfaceParamId(tsInterfaceParameter.getId());
        interfaceParamDto.setInterfaceId(tsInterfaceParameter.getInterfaceId());
        interfaceParamDto.setParameterType(tsInterfaceParameter.getParameterType());
        interfaceParamDto.setKey(tsInterfaceParameter.getKey());
        interfaceParamDto.setValue(tsInterfaceParameter.getValue());
        interfaceParamDto.setCreateTime(tsInterfaceParameter.getCreateTime());
        interfaceParamDto.setCreateBy(tsInterfaceParameter.getCreateBy());
        interfaceParamDto.setUpdateTime(tsInterfaceParameter.getUpdateTime());
        interfaceParamDto.setUpdateBy(tsInterfaceParameter.getUpdateBy());
        return interfaceParamDto;
    }

    private TsInterfaceParameter interfaceParamDtoConvertDb(InterfaceParamDto interfaceParamDto){
        TsInterfaceParameter tsInterfaceParameter = new TsInterfaceParameter();
        tsInterfaceParameter.setId(interfaceParamDto.getInterfaceParamId());
        tsInterfaceParameter.setInterfaceId(interfaceParamDto.getInterfaceId());
        tsInterfaceParameter.setParameterType(interfaceParamDto.getParameterType());
        tsInterfaceParameter.setKey(interfaceParamDto.getKey());
        tsInterfaceParameter.setValue(interfaceParamDto.getValue());
        tsInterfaceParameter.setCreateTime(interfaceParamDto.getCreateTime());
        tsInterfaceParameter.setCreateBy(interfaceParamDto.getCreateBy());
        tsInterfaceParameter.setUpdateTime(interfaceParamDto.getUpdateTime());
        tsInterfaceParameter.setUpdateBy(interfaceParamDto.getUpdateBy());
        return tsInterfaceParameter;
    }
}
