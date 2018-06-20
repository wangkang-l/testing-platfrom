package com.bgw.testing.common.enums;

public enum InterfaceParamType {
    QUERY("query", "QueryParam"),
    HEADER("header", "HeaderParam");

    public String type;
    public String description;

    InterfaceParamType(String type, String description){
        this.type = type;
        this.description = description;
    }
}
