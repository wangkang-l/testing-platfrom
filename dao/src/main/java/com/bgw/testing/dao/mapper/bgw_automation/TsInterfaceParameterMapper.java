package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsInterfaceParameter;

import java.util.List;
import java.util.Map;

public interface TsInterfaceParameterMapper {
    int deleteByPrimaryKey(String id);

    int deleteByInterfaceId(String interfaceId);

    int insert(TsInterfaceParameter record);

    int insertSelective(TsInterfaceParameter record);

    TsInterfaceParameter selectByPrimaryKey(String id);

    List<TsInterfaceParameter> selectByParams(Map params);

    int updateByPrimaryKeySelective(TsInterfaceParameter record);

    int updateByPrimaryKey(TsInterfaceParameter record);
}