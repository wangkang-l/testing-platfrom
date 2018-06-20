package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsInterfaceParameter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TsInterfaceParameterMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsInterfaceParameter record);

    int insertSelective(TsInterfaceParameter record);

    TsInterfaceParameter selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TsInterfaceParameter record);

    int updateByPrimaryKey(TsInterfaceParameter record);

    int deleteByInterfaceId(String interfaceId);

    List<TsInterfaceParameter> selectByInterfaceId(@Param(value="interfaceId") String interfaceId,
                                                   @Param(value="parameterType") String parameterType);
}