package com.bgw.testing.common.enums;

public enum CaseStatus {

    DISCARD("DISCARD", "弃用"),
    NORMAL("NORMAL", "正常"),
    UNDONE("UNDONE", "未完成");

    public String status;
    public String description;

    CaseStatus(String status, String description){
        this.status = status;
        this.description = description;
    }

}
