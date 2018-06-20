package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsTaskConfig;

import java.util.List;

public interface TsTaskConfigMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsTaskConfig record);

    int insertSelective(TsTaskConfig record);

    TsTaskConfig selectByPrimaryKey(String id);

    List<TsTaskConfig> selectAll();

    int updateByPrimaryKeySelective(TsTaskConfig record);

    int updateByPrimaryKey(TsTaskConfig record);
}