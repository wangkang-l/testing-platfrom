package com.bgw.testing.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    BAD_REQUEST("4000", "请求不正确", HttpStatus.BAD_REQUEST.value()),


    UNKNOWN_STEP_TYPE("4001", "未知的接口类型", HttpStatus.NOT_FOUND.value());

    public String errorKey;
    public String description;
    public Integer statusCode;

    ErrorCode(String errorKey,String description, Integer statusCode){
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
        return 0;
    }

}
