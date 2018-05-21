package com.bgw.testing.common.enums;

public enum StepTypeEnum {

    API("api", "接口请求"),
    SQL("sql", "sql请求"),
    REDIS("redis", "redis请求"),
    FUNC("func", "自定义方法"),
    BASIC("basic", "基本的步骤信息");

    public String key;
    public String description;

    StepTypeEnum(String key, String description){
        this.key = key;
        this.description = description;
    }

}
