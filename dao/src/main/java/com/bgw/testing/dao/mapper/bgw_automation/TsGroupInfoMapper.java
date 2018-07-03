package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsGroupInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TsGroupInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsGroupInfo record);

    int insertSelective(TsGroupInfo record);

    TsGroupInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TsGroupInfo record);

    int updateByPrimaryKey(TsGroupInfo record);

    List<TsGroupInfo> selectByParentId(String parentId);

    TsGroupInfo selectByGroupName(@Param(value = "groupName") String groupName, @Param(value = "parentId") String parentId);

    List<TsGroupInfo> selectLikeByGroupName(String groupName);

    List<TsGroupInfo> selectAll();

    int deleteByGroupIds(List<String> groupIds);

}