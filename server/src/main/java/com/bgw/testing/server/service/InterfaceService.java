package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.InterfaceInfoDto;
import com.bgw.testing.common.dto.InterfaceParamDto;
import com.bgw.testing.common.dto.PageInfo;
import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.common.enums.InterfaceParamType;
import com.bgw.testing.dao.mapper.bgw_automation.TsInterfaceBasicInfoMapper;
import com.bgw.testing.dao.mapper.bgw_automation.TsInterfaceParameterMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsInterfaceBasicInfo;
import com.bgw.testing.dao.pojo.bgw_automation.TsInterfaceParameter;
import com.bgw.testing.server.config.ServerException;
import com.bgw.testing.server.util.BaseStringUtils;
import com.bgw.testing.server.util.MultiFieldSorting;
import com.bgw.testing.server.util.PageConvert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterfaceService {
    @Autowired
    private TsInterfaceBasicInfoMapper tsInterfaceBasicInfoMapper;
    @Autowired
    private TsInterfaceParameterMapper tsInterfaceParameterMapper;
    @Autowired
    private GroupService groupService;

    /**
     * 获取当前组的接口并分页排序
     * @param groupId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<InterfaceInfoDto> getInterfaceInfo(String groupId, Integer pageNum, Integer pageSize, String var4) {
        Map params = new HashMap();
        params.put("groupId", groupId);

        //var4不为空，则进行模糊查询
        if (StringUtils.isNotBlank(var4)) {
            params.put("var4", var4);
        }

        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TsInterfaceBasicInfo> tsInterfaceBasicInfos = tsInterfaceBasicInfoMapper.selectByParams(params);
        if (tsInterfaceBasicInfos != null && tsInterfaceBasicInfos.size() > 0) {
            List<InterfaceInfoDto> interfaceInfoDtos = tsInterfaceBasicInfos.parallelStream()
                    .map(this::convertToInterfaceInfoDto)
                    .collect(Collectors.toList());
            return PageConvert.getPageInfo(page, sorting(interfaceInfoDtos));
        }
        return PageConvert.getPageInfo(page, new ArrayList<>());
    }

    /**
     * 移动接口
     * @param interfaceIds
     * @param newGroupId
     */
    public void moveInterface(List<String> interfaceIds, String newGroupId) {
        if (interfaceIds == null || interfaceIds.size() == 0) {
            throw new ServerException(ErrorCode.NOT_NULL, "接口ID");
        }
        if (!groupService.isExistGroup(newGroupId)) {
            throw new ServerException(ErrorCode.NOT_EXIST, "组ID" + newGroupId);
        }
        interfaceIds.parallelStream().forEach(interfaceId -> {
            TsInterfaceBasicInfo tsInterfaceBasicInfo = tsInterfaceBasicInfoMapper.selectByPrimaryKey(interfaceId);
            if (tsInterfaceBasicInfo != null) {
                tsInterfaceBasicInfo.setGroupId(newGroupId);
                tsInterfaceBasicInfo.setUpdateTime(new Date());
                tsInterfaceBasicInfo.setUpdateBy("System");
                tsInterfaceBasicInfoMapper.updateByPrimaryKeySelective(tsInterfaceBasicInfo);
            }
        });
    }

    /**
     * 接口排序
     * @param interfaceInfoDtos
     * @return
     */
    private List<InterfaceInfoDto> sorting(List<InterfaceInfoDto> interfaceInfoDtos) {
        String[] fields = new String[]{"interfacePath", "name"};
        String[] orders = new String[]{"asc", "asc"};
        interfaceInfoDtos.sort(new MultiFieldSorting(fields, orders));
        return interfaceInfoDtos;
    }

    /**
     * 新增接口信息
     * @param interfaceInfoDto
     */
    @Transactional
    public void addInterfaceInfo(InterfaceInfoDto interfaceInfoDto) {
        if (groupService.isExistGroup(interfaceInfoDto.getGroupId()) ) {
            interfaceInfoDto.setInterfaceId(BaseStringUtils.uuidSimple());
            TsInterfaceBasicInfo tsInterfaceBasicInfo = convertToTsInterfaceBasicInfo(interfaceInfoDto);
            checkInterfaceName(interfaceInfoDto);
            tsInterfaceBasicInfo.setCreateTime(new Date());
            tsInterfaceBasicInfo.setCreateBy("System");
            tsInterfaceBasicInfoMapper.insertSelective(tsInterfaceBasicInfo);
            addInterfaceParam(interfaceInfoDto);
        } else {
            throw new ServerException(ErrorCode.NOT_EXIST, interfaceInfoDto.getGroupId());
        }
    }

    /**
     * 校验当前组内是否存在相同接口名称
     * @param interfaceInfoDto
     */
    private void checkInterfaceName(InterfaceInfoDto interfaceInfoDto) {
        List<TsInterfaceBasicInfo> tsInterfaceBasicInfos
                = tsInterfaceBasicInfoMapper.selectByGroupIdAndInterfaceNameAndId(
                interfaceInfoDto.getGroupId(),
                interfaceInfoDto.getName(),
                interfaceInfoDto.getInterfaceId());
        if (tsInterfaceBasicInfos != null && tsInterfaceBasicInfos.size() > 0) {
            throw new ServerException(ErrorCode.ALREADY_EXISTS, "接口名称：" + interfaceInfoDto.getName());
        }
    }

    /**
     * 删除接口信息和参数表
     * @param interfaceId
     */
    @Transactional
    public void deleteInterfaceInfo(String interfaceId) {
        tsInterfaceBasicInfoMapper.deleteByPrimaryKey(interfaceId);
        tsInterfaceParameterMapper.deleteByInterfaceId(interfaceId);
    }

    /**
     * 更新接口信息
     * @param interfaceInfoDto
     */
    @Transactional
    public void updateInterfaceInfo(String interfaceId, InterfaceInfoDto interfaceInfoDto) {
        TsInterfaceBasicInfo tsInterfaceBasicInfo = tsInterfaceBasicInfoMapper.selectByPrimaryKey(interfaceId);
        if (tsInterfaceBasicInfo != null) {
            checkInterfaceName(interfaceInfoDto);
            tsInterfaceBasicInfo = convertToTsInterfaceBasicInfo(interfaceInfoDto);
            tsInterfaceBasicInfo.setUpdateTime(new Date());
            tsInterfaceBasicInfo.setUpdateBy("System");
            tsInterfaceBasicInfoMapper.updateByPrimaryKeySelective(convertToTsInterfaceBasicInfo(interfaceInfoDto));
            tsInterfaceParameterMapper.deleteByInterfaceId(interfaceInfoDto.getInterfaceId());
            addInterfaceParam(interfaceInfoDto);
        } else {
            throw new ServerException(ErrorCode.NOT_EXIST, "接口ID：" + interfaceId);
        }
    }

    /**
     * 新增接口参数
     * @param interfaceInfoDto
     */
    private void addInterfaceParam(InterfaceInfoDto interfaceInfoDto) {
        if (interfaceInfoDto.getQuerys() != null && interfaceInfoDto.getQuerys().size() > 0) {
            interfaceInfoDto.getQuerys().parallelStream().forEach(interfaceParamDto -> {
                        addInterfaceParam(interfaceInfoDto.getInterfaceId(), interfaceParamDto);
            });
        }
        if (interfaceInfoDto.getHeaders() != null && interfaceInfoDto.getHeaders().size() > 0) {
            interfaceInfoDto.getHeaders().parallelStream().forEach(interfaceParamDto -> {
                addInterfaceParam(interfaceInfoDto.getInterfaceId(), interfaceParamDto);
            });
        }
    }

    private void addInterfaceParam(String interfaceId, InterfaceParamDto interfaceParamDto) {
        TsInterfaceParameter tsInterfaceParameter = convertToTsInterfaceParameter(interfaceParamDto);
        tsInterfaceParameter.setId(BaseStringUtils.uuidSimple());
        tsInterfaceParameter.setInterfaceId(interfaceId);
        tsInterfaceParameter.setCreateTime(new Date());
        tsInterfaceParameter.setCreateBy("System");
        tsInterfaceParameterMapper.insertSelective(tsInterfaceParameter);
    }

    private InterfaceInfoDto convertToInterfaceInfoDto(TsInterfaceBasicInfo tsInterfaceBasicInfo){
        InterfaceInfoDto interfaceInfoDto = new InterfaceInfoDto();
        interfaceInfoDto.setInterfaceId(tsInterfaceBasicInfo.getId());
        interfaceInfoDto.setGroupId(tsInterfaceBasicInfo.getGroupId());
        interfaceInfoDto.setName(tsInterfaceBasicInfo.getInterfaceName());
        interfaceInfoDto.setDescription(tsInterfaceBasicInfo.getInterfaceDescription());
        interfaceInfoDto.setPath(tsInterfaceBasicInfo.getPath());
        interfaceInfoDto.setMethod(tsInterfaceBasicInfo.getMethod());
        interfaceInfoDto.setBody(tsInterfaceBasicInfo.getBody());
        interfaceInfoDto.getHeaders().addAll(getParam(tsInterfaceBasicInfo.getId(), InterfaceParamType.HEADER.type));
        interfaceInfoDto.getQuerys().addAll(getParam(tsInterfaceBasicInfo.getId(), InterfaceParamType.QUERY.type));
        return interfaceInfoDto;
    }

    private TsInterfaceBasicInfo convertToTsInterfaceBasicInfo(InterfaceInfoDto interfaceInfoDto){
        TsInterfaceBasicInfo tsInterfaceBasicInfo = new TsInterfaceBasicInfo();
        tsInterfaceBasicInfo.setId(interfaceInfoDto.getInterfaceId());
        tsInterfaceBasicInfo.setGroupId(interfaceInfoDto.getGroupId());
        tsInterfaceBasicInfo.setInterfaceName(interfaceInfoDto.getName());
        tsInterfaceBasicInfo.setInterfaceDescription(interfaceInfoDto.getDescription());
        tsInterfaceBasicInfo.setPath(interfaceInfoDto.getPath());
        tsInterfaceBasicInfo.setMethod(interfaceInfoDto.getMethod());
        tsInterfaceBasicInfo.setBody(interfaceInfoDto.getBody());
        return tsInterfaceBasicInfo;
    }

    private InterfaceParamDto convertToInterfaceParamDto(TsInterfaceParameter tsInterfaceParameter) {
        InterfaceParamDto interfaceParamDto = new InterfaceParamDto();
        interfaceParamDto.setKey(tsInterfaceParameter.getKey());
        interfaceParamDto.setValue(tsInterfaceParameter.getValue());
        interfaceParamDto.setDescription(tsInterfaceParameter.getDescription());
        return interfaceParamDto;
    }

    private TsInterfaceParameter convertToTsInterfaceParameter(InterfaceParamDto interfaceParamDto) {
        TsInterfaceParameter tsInterfaceParameter = new TsInterfaceParameter();
        tsInterfaceParameter.setKey(interfaceParamDto.getKey());
        tsInterfaceParameter.setValue(interfaceParamDto.getValue());
        tsInterfaceParameter.setDescription(interfaceParamDto.getDescription());
        return tsInterfaceParameter;
    }

    private List<InterfaceParamDto> getParam(String interfaceId, String parameterType){
        Map params = new HashMap();
        params.put("interfaceId", interfaceId);
        params.put("parameterType", parameterType);
        List<TsInterfaceParameter> tsInterfaceParameters = tsInterfaceParameterMapper.selectByParams(params);
        if (tsInterfaceParameters != null && tsInterfaceParameters.size() > 0) {
            return tsInterfaceParameters.parallelStream()
                    .map(this::convertToInterfaceParamDto)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}