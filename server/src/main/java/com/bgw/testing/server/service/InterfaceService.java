package com.bgw.testing.server.service;

import com.bgw.testing.common.dto.InterfaceInfoDto;
import com.bgw.testing.common.dto.PageInfo;
import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.common.enums.InterfaceParamType;
import com.bgw.testing.dao.mapper.bgw_automation.TsInterfaceBasicInfoMapper;
import com.bgw.testing.dao.mapper.bgw_automation.TsInterfaceParameterMapper;
import com.bgw.testing.dao.pojo.bgw_automation.TsInterfaceBasicInfo;
import com.bgw.testing.dao.pojo.bgw_automation.TsInterfaceParameter;
import com.bgw.testing.server.config.ServerException;
import com.bgw.testing.server.util.BaseJsonUtils;
import com.bgw.testing.server.util.BaseStringUtils;
import com.bgw.testing.server.util.MultiFieldSorting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class InterfaceService {
    @Autowired
    private TsInterfaceBasicInfoMapper tsInterfaceBasicInfoMapper;
    @Autowired
    private TsInterfaceParameterMapper tsInterfaceParameterMapper;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RedisService redisService;

    private static final String REDIS_INTERFACE = "interface_";
    private static final Integer DEFAULT_REDIS_DB = 12;

    @PostConstruct
    public void initInterfaceInfo() {
        redisService.delPattern(DEFAULT_REDIS_DB, REDIS_INTERFACE + "*");
        List<TsInterfaceBasicInfo> tsInterfaceBasicInfos = tsInterfaceBasicInfoMapper.selectAll();
        if (tsInterfaceBasicInfos != null && tsInterfaceBasicInfos.size() > 0) {
            tsInterfaceBasicInfos.forEach(tsInterfaceBasicInfo -> {
                redisService.hSet(
                        DEFAULT_REDIS_DB,
                        REDIS_INTERFACE + tsInterfaceBasicInfo.getGroupId(),
                        tsInterfaceBasicInfo.getId(),
                        convertToInterfaceInfoDto(tsInterfaceBasicInfo)
                );
            });
        }
    }

    /**
     * 获取当前组的接口并分页排序
     * @param groupId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<InterfaceInfoDto> getInterfaceInfo(String groupId, Integer pageNum, Integer pageSize) {
        return new PageInfo<>(sorting(getInterfaceInfoByGroupId(groupId)), pageNum, pageSize);
    }

    /**
     * 获取当前组的接口
     * @param groupId
     * @return
     */
    private List<InterfaceInfoDto> getInterfaceInfoByGroupId(String groupId)  {
        Map<String, String> data = redisService.hGetAll(DEFAULT_REDIS_DB, REDIS_INTERFACE + groupId);
        List<InterfaceInfoDto> interfaceInfoDtos = new ArrayList<>();
        data.keySet().forEach(key -> {
            interfaceInfoDtos.add(BaseJsonUtils.readValue(data.get(key), InterfaceInfoDto.class));
        });
        return interfaceInfoDtos;
    }


    /**
     * 接口排序
     * @param interfaceInfoDtos
     * @return
     */
    private List<InterfaceInfoDto> sorting(List<InterfaceInfoDto> interfaceInfoDtos) {
        String[] fields = new String[]{"updateTime"};
        String[] orders = new String[]{"desc"};
        interfaceInfoDtos.sort(new MultiFieldSorting(fields, orders));
        return interfaceInfoDtos;
    }

    /**
     * 新增接口信息
     * @param interfaceInfoDto
     */
    @Transactional
    public void addInterfaceInfo(InterfaceInfoDto interfaceInfoDto) {
        if(groupService.isExistGroup(interfaceInfoDto.getGroupId())){
            TsInterfaceBasicInfo tsInterfaceBasicInfo = convertToTsInterfaceBasicInfo(interfaceInfoDto);
            tsInterfaceBasicInfo.setId(BaseStringUtils.uuidSimple());
            tsInterfaceBasicInfo.setCreateTime(new Date());
            tsInterfaceBasicInfo.setCreateBy("System");
            tsInterfaceBasicInfo.setUpdateTime(new Date());
            tsInterfaceBasicInfo.setUpdateBy("System");
            tsInterfaceBasicInfoMapper.insertSelective(tsInterfaceBasicInfo);
            interfaceInfoDto.setInterfaceId(tsInterfaceBasicInfo.getId());
            isExistInterfaceParam(interfaceInfoDto);
            redisService.hSet(DEFAULT_REDIS_DB, REDIS_INTERFACE + interfaceInfoDto.getGroupId(), interfaceInfoDto.getInterfaceId(), interfaceInfoDto);

        }
        else{
            throw new ServerException(ErrorCode.NOT_EXIST,interfaceInfoDto.getGroupId());
        }

    }

    /**
     * 删除接口信息和参数表
     * @param interfaceId
     */
    @Transactional
    public void deleteInterfaceInfo(String groupId,String interfaceId) {
        tsInterfaceBasicInfoMapper.deleteByPrimaryKey(interfaceId);
        tsInterfaceParameterMapper.deleteByInterfaceId(interfaceId);
        redisService.hDel(DEFAULT_REDIS_DB,REDIS_INTERFACE+groupId,interfaceId);
    }

    /**
     * 更新接口信息
     * @param interfaceInfoDto
     */
    @Transactional
    public void updateInterfaceInfo(InterfaceInfoDto interfaceInfoDto) {
        String oldGrouId = tsInterfaceBasicInfoMapper.selectByPrimaryKey(interfaceInfoDto.getInterfaceId()).getGroupId();
        if(!oldGrouId.equals(interfaceInfoDto.getGroupId())){
            redisService.hDel(DEFAULT_REDIS_DB,REDIS_INTERFACE + oldGrouId,interfaceInfoDto.getInterfaceId());
        }

        interfaceInfoDto.setUpdateTime(new Date());
        interfaceInfoDto.setUpdateBy("System");
        tsInterfaceBasicInfoMapper.updateByPrimaryKeySelective(convertToTsInterfaceBasicInfo(interfaceInfoDto));
        tsInterfaceParameterMapper.deleteByInterfaceId(interfaceInfoDto.getInterfaceId());
        isExistInterfaceParam(interfaceInfoDto);

        redisService.hSet(DEFAULT_REDIS_DB, REDIS_INTERFACE + interfaceInfoDto.getGroupId(), interfaceInfoDto.getInterfaceId(), interfaceInfoDto);
    }

    /**
     * 新增接口参数
     * @param interfaceId
     * @param type
     * @param key
     * @param value
     */
    private void addInterfaceParam(String interfaceId,String type, String key,String value) {
        TsInterfaceParameter tsInterfaceParameter = new TsInterfaceParameter();
        tsInterfaceParameter.setId(BaseStringUtils.uuidSimple());
        tsInterfaceParameter.setInterfaceId(interfaceId);
        tsInterfaceParameter.setParameterType(type);
        tsInterfaceParameter.setKey(key);
        tsInterfaceParameter.setValue(value);
        tsInterfaceParameter.setCreateTime(new Date());
        tsInterfaceParameter.setCreateBy("System");
        tsInterfaceParameter.setUpdateTime(new Date());
        tsInterfaceParameter.setUpdateBy("System");
        tsInterfaceParameterMapper.insertSelective(tsInterfaceParameter);
    }

    private void isExistInterfaceParam(InterfaceInfoDto interfaceInfoDto){
        if(interfaceInfoDto.getHeaders().size()>0){
            Iterator it = interfaceInfoDto.getHeaders().entrySet().iterator();
            while(it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                addInterfaceParam(interfaceInfoDto.getInterfaceId(),InterfaceParamType.HEADER.type,key,value);
            }
        }
        if(interfaceInfoDto.getQuerys().size()>0){
            Iterator it = interfaceInfoDto.getQuerys().entrySet().iterator();
            while(it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                addInterfaceParam(interfaceInfoDto.getInterfaceId(),InterfaceParamType.QUERY.type,key,value);
            }
        }
    }

    private InterfaceInfoDto convertToInterfaceInfoDto(TsInterfaceBasicInfo tsInterfaceBasicInfo){
        InterfaceInfoDto interfaceInfoDto = new InterfaceInfoDto();
        interfaceInfoDto.setInterfaceId(tsInterfaceBasicInfo.getId());
        interfaceInfoDto.setGroupId(tsInterfaceBasicInfo.getGroupId());
        interfaceInfoDto.setDescription(tsInterfaceBasicInfo.getDescription());
        interfaceInfoDto.setPath(tsInterfaceBasicInfo.getPath());
        interfaceInfoDto.setMethod(tsInterfaceBasicInfo.getMethod());
        interfaceInfoDto.setBody(tsInterfaceBasicInfo.getBody());
        interfaceInfoDto.setCreateTime(tsInterfaceBasicInfo.getCreateTime());
        interfaceInfoDto.setCreateBy(tsInterfaceBasicInfo.getCreateBy());
        interfaceInfoDto.setUpdateTime(tsInterfaceBasicInfo.getUpdateTime());
        interfaceInfoDto.setUpdateBy(tsInterfaceBasicInfo.getUpdateBy());
        interfaceInfoDto.setHeaders(getParam(tsInterfaceBasicInfo.getId(),InterfaceParamType.HEADER.type));
        interfaceInfoDto.setQuerys(getParam(tsInterfaceBasicInfo.getId(),InterfaceParamType.QUERY.type));
        return interfaceInfoDto;
    }

    private Map<String, String> getParam(String interface_id, String parameter_type){
        List<TsInterfaceParameter> tsInterfaceParameters = tsInterfaceParameterMapper.selectByInterfaceId(interface_id,parameter_type);
        Map<String, String> params = new HashMap<>();
        if (tsInterfaceParameters != null && tsInterfaceParameters.size() > 0) {
            tsInterfaceParameters.forEach(tsInterfaceParameter -> {
                params.put(tsInterfaceParameter.getKey(),tsInterfaceParameter.getValue());
            });
        }
        return params;
    }

    private TsInterfaceBasicInfo convertToTsInterfaceBasicInfo(InterfaceInfoDto interfaceInfoDto){
        TsInterfaceBasicInfo tsInterfaceBasicInfo = new TsInterfaceBasicInfo();
        tsInterfaceBasicInfo.setId(interfaceInfoDto.getInterfaceId());
        tsInterfaceBasicInfo.setGroupId(interfaceInfoDto.getGroupId());
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
}