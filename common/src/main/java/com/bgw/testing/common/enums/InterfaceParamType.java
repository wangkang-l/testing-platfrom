package com.bgw.testing.common.enums;

public enum InterfaceParamType {
    QUERY("QUERY", "query类型参数"),
    HEADER("HEADER", "header头参数");

    public String type;
    public String description;

    InterfaceParamType(String type, String description){
        this.type = type;
        this.description = description;
    }
}
