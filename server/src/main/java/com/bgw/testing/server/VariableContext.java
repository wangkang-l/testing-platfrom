package com.bgw.testing.server;

import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.common.enums.VariableType;
import com.bgw.testing.dao.pojo.bgw_automation.TsVariableList;
import com.bgw.testing.server.config.ServerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class VariableContext {

    private static List<TsVariableList> variableLists = new ArrayList<>();

    private static Map<String, String> globalVariable = new ConcurrentHashMap<>();
    private static Map<String, String> environmentVariable = new ConcurrentHashMap<>();
    private static Map<String, Map<String, String>> temporaryVariable = new ConcurrentHashMap<>();

    private VariableContext(){
    }

    public static VariableContext getInstance(){
        return VariableContextHolder.vInstance;
    }

    private static class VariableContextHolder {
        private static final VariableContext vInstance = new VariableContext();
    }

    public List<TsVariableList> getAllVariableList() {
        return variableLists;
    }

    public Map<String, String> getGlobalVariable() {
        return globalVariable;
    }

    public Map<String, String> getEnvironmentVariable() {
        return environmentVariable;
    }

    public Map<String, Map<String, String>> getTemporaryVariable() {
        return temporaryVariable;
    }

    public void initTemporaryVariable(String key, Map<String, String> map) {
        if (map != null && map.size() > 0) {
            temporaryVariable.putIfAbsent(key, map);
        }
    }

    public Map<String, String> getTemporaryVariable(String key) {
        temporaryVariable.putIfAbsent(key, new ConcurrentHashMap<>());
        return temporaryVariable.get(key);
    }

    public void initVariable(List<TsVariableList> tsVariableLists) {
        if (tsVariableLists != null && tsVariableLists.size() > 0) {
            variableLists.addAll(tsVariableLists);
            environmentVariable.clear();
            tsVariableLists.parallelStream().forEach(tsVariableList -> {
                if (tsVariableList.getType().equals(VariableType.GLOBAL.type)) {
                    globalVariable.put(tsVariableList.getConfigKey(), tsVariableList.getConfigValue());
                } else if (tsVariableList.getType().equals(VariableType.ENVIRONMENT.type)) {
                    environmentVariable.put(tsVariableList.getConfigKey(), tsVariableList.getConfigValue());
                }
            });
        }
    }

    public void addOrUpdateVariable(TsVariableList tsVariableList, boolean isUpdate) {

        if (isUpdate) {
            List<TsVariableList> oldVariable = variableLists.stream()
                    .filter(temp -> temp.getId().equals(tsVariableList.getId()))
                    .collect(Collectors.toList());
            if (oldVariable != null && oldVariable.size() == 1) {
                delVariable(oldVariable.get(0));
            } else {
                throw new ServerException(ErrorCode.NOT_EXIST, "variable");
            }
        }

        variableLists.add(tsVariableList);

        if (tsVariableList.getType().equals(VariableType.GLOBAL.type)) {
            globalVariable.put(tsVariableList.getConfigKey(), tsVariableList.getConfigValue());
        } else if (tsVariableList.getType().equals(VariableType.ENVIRONMENT.type)) {
            environmentVariable.put(tsVariableList.getConfigKey(), tsVariableList.getConfigValue());
        }

    }

    public void delVariable(TsVariableList tsVariableList) {
        if (tsVariableList != null) {
            variableLists.removeIf(temp -> temp.getId().equals(tsVariableList.getId()));
            if (tsVariableList.getType().equals(VariableType.GLOBAL.type)) {
                globalVariable.remove(tsVariableList.getConfigKey());
            } else if (tsVariableList.getType().equals(VariableType.ENVIRONMENT.type)) {
                environmentVariable.remove(tsVariableList.getConfigKey());
            }
        }
    }

}
