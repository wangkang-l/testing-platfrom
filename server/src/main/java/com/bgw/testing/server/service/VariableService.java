package com.bgw.testing.server.service;

import com.bgw.testing.dao.bgw_automation.mapper.TsVariableListMapper;
import com.bgw.testing.dao.bgw_automation.pojo.TsVariableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariableService {

    @Autowired
    private TsVariableListMapper tsVariableListMapper;

    /**
     * 根据listId 与 variableType 获取变量列表
     * @param listId
     * @param variableType
     * @return
     */
    public List<TsVariableList> getVariableList(String listId, String variableType) {
        return tsVariableListMapper.selectByCaseIdAndType(listId, variableType);
    }

}
