package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsTaskProperty;

import java.util.List;

public interface TsTaskPropertyMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsTaskProperty record);

    int insertSelective(TsTaskProperty record);

    TsTaskProperty selectByPrimaryKey(String id);

    List<TsTaskProperty> selectByTaskId(String taskId);

    int updateByPrimaryKeySelective(TsTaskProperty record);

    int updateByPrimaryKey(TsTaskProperty record);

    int deleteByTaskId(String taskId);
}