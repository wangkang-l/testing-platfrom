package com.bgw.testing.server.config;

import com.bgw.testing.common.enums.ErrorCode;
import lombok.Getter;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public class ServerException extends RuntimeException {

    @Getter
    private String errorKey;
    @Getter
    private String errorMsg;

    public ServerException(String errorKey, String errorMsg) {
        this.errorKey = errorKey;
        this.errorMsg = errorMsg;
    }

    public ServerException(ErrorCode errorCode) {
        this.errorKey = errorCode.errorKey;
        this.errorMsg = errorCode.description;
    }

    public ServerException(ErrorCode errorCode, Object... args) {
        FormattingTuple ft = MessageFormatter.arrayFormat(errorCode.description, args);
        this.errorKey = errorCode.errorKey;
        this.errorMsg = ft.getMessage();
    }

    public ServerException(){}

}
