package com.bgw.testing.common.enums;

public enum CaseStatus {

    DISCARD("discard", "弃用"),
    NORMAL("normal", "正常"),
    UNDONE("undone", "未完成");

    private String status;
    private String description;

    CaseStatus(String status, String description){
        this.status = status;
        this.description = description;
    }

}
