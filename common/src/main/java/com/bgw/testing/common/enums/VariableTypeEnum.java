package com.bgw.testing.common.enums;

public enum VariableTypeEnum {

    GLOBAL("global", "全局变量"),
    ENVIRONMENT("environment", "环境变量"),
    CASE("case", "临时用例变量");

    public String key;
    public String description;

    VariableTypeEnum(String key, String description){
        this.key = key;
        this.description = description;
    }

}
