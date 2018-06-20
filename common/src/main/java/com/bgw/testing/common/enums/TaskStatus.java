package com.bgw.testing.common.enums;

import lombok.Getter;

public enum TaskStatus {

    INIT("INIT", "初始化"),
    ENABLED("ENABLED", "启用"),
    DISABLED("DISABLED", "停用");

    @Getter
    private String key;
    @Getter
    private String value;

    TaskStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(String key) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.getKey().equalsIgnoreCase(key)) {
                return taskStatus.getValue();
            }
        }
        return null;
    }

}
