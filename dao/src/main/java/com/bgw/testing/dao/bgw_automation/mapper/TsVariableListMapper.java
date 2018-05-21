package com.bgw.testing.dao.bgw_automation.mapper;

import com.bgw.testing.dao.bgw_automation.pojo.TsVariableList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TsVariableListMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsVariableList record);

    int insertSelective(TsVariableList record);

    TsVariableList selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TsVariableList record);

    int updateByPrimaryKey(TsVariableList record);

    List<TsVariableList> selectByCaseIdAndType(@Param("listId") String listId, @Param("type") String type);
}