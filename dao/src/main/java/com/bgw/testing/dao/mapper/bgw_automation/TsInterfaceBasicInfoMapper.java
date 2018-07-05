package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsInterfaceBasicInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TsInterfaceBasicInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsInterfaceBasicInfo record);

    int insertSelective(TsInterfaceBasicInfo record);

    TsInterfaceBasicInfo selectByPrimaryKey(String id);

    List<TsInterfaceBasicInfo> selectByGroupIdAndInterfaceNameAndId(@Param(value = "groupId") String groupId,
                                                               @Param(value = "interfaceName") String interfaceName,
                                                               @Param(value = "id") String id);

    int updateByPrimaryKeySelective(TsInterfaceBasicInfo record);

    int updateByPrimaryKey(TsInterfaceBasicInfo record);

    List<TsInterfaceBasicInfo> selectByParams(Map params);
}