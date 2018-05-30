package com.bgw.testing.server.config;

import com.bgw.testing.common.enums.ErrorCode;
import lombok.Data;

@Data
public class ServerException extends RuntimeException {

    private String errorKey;
    private String errorMsg;

    public ServerException(String errorKey, String errorMsg) {
        this.errorKey = errorKey;
        this.errorMsg = errorMsg;
    }

    public ServerException(String errorKey) {
        this.errorKey = errorKey;
        this.errorMsg = ErrorCode.getDescription(errorKey);
    }

    public ServerException(){}

}
