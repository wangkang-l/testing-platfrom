package com.bgw.testing.common.enums;

public enum StepType {

    HTTP("http", "http请求"),
    MYSQL("mysql", "sql请求"),
    REDIS("redis", "redis请求"),
    FUNC("func", "自定义方法"),
    TEMPLATE("template", "模板");

    public String type;
    public String description;

    StepType(String type, String description){
        this.type = type;
        this.description = description;
    }

}
