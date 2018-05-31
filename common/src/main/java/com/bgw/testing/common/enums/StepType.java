package com.bgw.testing.common.enums;

public enum StepType {

    INTERFACE("inteface", "接口请求"),
    MYSQL("mysql", "sql请求"),
    REDIS("redis", "redis请求"),
    FUNC("func", "自定义方法"),
    TEMPLATE("template", "模板");

    private String type;
    private String description;

    StepType(String type, String description){
        this.type = type;
        this.description = description;
    }

}
