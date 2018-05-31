package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsVariableList;

import java.util.List;
import java.util.Map;

public interface TsVariableListMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsVariableList record);

    int insertSelective(TsVariableList record);

    TsVariableList selectByPrimaryKey(String id);

    List<TsVariableList> selectAll();

    int updateByPrimaryKeySelective(TsVariableList record);

    int updateByPrimaryKey(TsVariableList record);

    List<TsVariableList> selectVariableList(Map<String, Object> params);
}