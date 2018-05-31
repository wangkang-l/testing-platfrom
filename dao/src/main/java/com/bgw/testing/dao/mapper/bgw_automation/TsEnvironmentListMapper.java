package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsEnvironmentList;

public interface TsEnvironmentListMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsEnvironmentList record);

    int insertSelective(TsEnvironmentList record);

    TsEnvironmentList selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TsEnvironmentList record);

    int updateByPrimaryKey(TsEnvironmentList record);
}