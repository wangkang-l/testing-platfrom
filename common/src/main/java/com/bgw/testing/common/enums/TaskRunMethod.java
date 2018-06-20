package com.bgw.testing.common.enums;

import lombok.Getter;

public enum TaskRunMethod {

    TIMING("TIMING", "定时任务"),
    TEMPORARY("TEMPORARY", "临时任务");

    @Getter
    private String key;
    @Getter
    private String value;

    TaskRunMethod(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(String key) {
        for (TaskRunMethod taskRunMethod : TaskRunMethod.values()) {
            if (taskRunMethod.getKey().equalsIgnoreCase(key)) {
                return taskRunMethod.getValue();
            }
        }
        return null;
    }

}
