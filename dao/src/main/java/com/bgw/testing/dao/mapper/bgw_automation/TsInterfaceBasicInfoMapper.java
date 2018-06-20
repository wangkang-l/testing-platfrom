package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsInterfaceBasicInfo;
import java.util.List;

public interface TsInterfaceBasicInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsInterfaceBasicInfo record);

    int insertSelective(TsInterfaceBasicInfo record);

    TsInterfaceBasicInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TsInterfaceBasicInfo record);

    int updateByPrimaryKeyWithBLOBs(TsInterfaceBasicInfo record);

    int updateByPrimaryKey(TsInterfaceBasicInfo record);

    List<TsInterfaceBasicInfo> selectByDescription(String description);

    List<TsInterfaceBasicInfo> selectAll();
}