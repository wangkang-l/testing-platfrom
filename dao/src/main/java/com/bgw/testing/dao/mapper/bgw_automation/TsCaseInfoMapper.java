package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsCaseInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TsCaseInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsCaseInfo record);

    int insertSelective(TsCaseInfo record);

    TsCaseInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TsCaseInfo record);

    int updateByPrimaryKey(TsCaseInfo record);

    List<TsCaseInfo> selectAll();

    List<TsCaseInfo> selectByParams(Map params);

    int updateGroupId(@Param(value = "caseIds") List<String> caseIds, @Param(value = "groupId") String groupId);
}