package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsCaseInfo;

import java.util.List;

public interface TsCaseInfoMapper {

    int deleteByPrimaryKey(String id);

    int insert(TsCaseInfo record);

    int insertSelective(TsCaseInfo record);

    TsCaseInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TsCaseInfo record);

    int updateByPrimaryKey(TsCaseInfo record);

    List<TsCaseInfo> selectByGroupId(String groupId);

    List<TsCaseInfo> selectAll();

}