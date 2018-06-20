package com.bgw.testing.common.enums;


public enum ErrorCode {

    BAD_REQUEST("4000", "系统异常", 400),
    UNKNOWN_TYPE("4001", "未知的[{}]类型", 400),
    NOT_EXIST("4002", "不存在的[{}]", 400),
    ALREADY_EXISTS("4003", "[{}]已经存在", 400),
    INVALID_CONDITION("4004", "无效的条件表达式[{}]", 400),
    NOT_NULL("4005", "[{}]不能为空", 400),
    FORMAT_ERROR("4006", "内容[{}]格式错误", 400),
    NON_ENABLED("4007", "[{}]未启用", 400);


    public String errorKey;
    public String description;
    public Integer statusCode;

    ErrorCode(String errorKey, String description, Integer statusCode){
        this.errorKey = errorKey;
        this.description = description;
        this.statusCode = statusCode;
    }

    public static String getDescription(String errorKey){
        for(ErrorCode errorCode : ErrorCode.values()){
            if(errorCode.errorKey.equals(errorKey)){
                return errorCode.description;
            }
        }
        return null;
    }

    public static Integer getStatusCode(String errorKey){
        for(ErrorCode errorCode : ErrorCode.values()){
            if(errorCode.errorKey.equals(errorKey)){
                return errorCode.statusCode;
            }
        }
        return 400;
    }

}
