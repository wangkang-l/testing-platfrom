package com.bgw.testing.server.config;

import lombok.Getter;
import lombok.Setter;

public class ServerException extends RuntimeException {

    @Getter
    @Setter
    private String errorKey;
    @Getter
    @Setter
    private String errorMsg;

    private ServerException(String errorKey, String errorMsg) {
        this.errorKey = errorKey;
        this.errorMsg = errorMsg;
    }

    public static ServerException fromKey(String errorKey, String errorMsg) {
        return new ServerException(errorKey, errorMsg);
    }
/**/
}
