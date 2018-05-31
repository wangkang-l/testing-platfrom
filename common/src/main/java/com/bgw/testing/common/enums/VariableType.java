package com.bgw.testing.common.enums;

public enum VariableType {

    GLOBAL("global", "全局变量"),
    ENVIRONMENT("environment", "环境变量"),
    TEMPORARY("temporary", "临时变量");

    public String type;
    public String description;

    VariableType(String type, String description){
        this.type = type;
        this.description = description;
    }

}
