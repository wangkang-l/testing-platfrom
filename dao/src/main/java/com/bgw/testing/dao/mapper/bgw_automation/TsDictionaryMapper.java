package com.bgw.testing.dao.mapper.bgw_automation;

import com.bgw.testing.dao.pojo.bgw_automation.TsDictionary;

import java.util.List;

public interface TsDictionaryMapper {
    int deleteByPrimaryKey(String id);

    int insert(TsDictionary record);

    int insertSelective(TsDictionary record);

    TsDictionary selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TsDictionary record);

    int updateByPrimaryKey(TsDictionary record);

    List<TsDictionary> selectByDictType(String dictType);
}