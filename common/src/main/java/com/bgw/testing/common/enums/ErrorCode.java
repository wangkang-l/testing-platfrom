package com.bgw.testing.common.enums;


public enum ErrorCode {

    BAD_REQUEST("4000", "系统异常", 400),
    UNKNOWN_TYPE("4001", "未知的[{}]类型", 401),
    NOT_EXIST("4002", "不存在的[{}]", 402),
    ALREADY_EXISTS("4003", "[{}]已经存在", 403);

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
