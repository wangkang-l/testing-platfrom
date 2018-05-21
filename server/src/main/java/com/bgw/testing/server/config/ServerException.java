package com.bgw.testing.server.config;

import lombok.Data;

@Data
public class ServerException extends RuntimeException {


    private String errorKey;

    private String errorMsg;

    private ServerException(String errorKey, String errorMsg) {
        this.errorKey = errorKey;
        this.errorMsg = errorMsg;
    }

    public static ServerException fromKey(String errorKey, String errorMsg) {
        return new ServerException(errorKey, errorMsg);
    }

}
