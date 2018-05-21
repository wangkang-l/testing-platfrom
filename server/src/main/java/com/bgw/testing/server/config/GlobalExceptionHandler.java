package com.bgw.testing.server.config;

import com.bgw.testing.common.ErrorCode;
import com.bgw.testing.common.ErrorInfo;
import com.bgw.testing.server.util.BaseMDCUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ServerException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ServerException e) {
        ErrorInfo<String> errorInfo = new ErrorInfo<>();
        errorInfo.setMsg(ErrorCode.getDescription(e.getErrorKey()) + ":" + e.getErrorMsg());
        errorInfo.setCode(e.getErrorKey());
        errorInfo.setCorrelationId(BaseMDCUtils.get());
        BaseMDCUtils.clear();

        httpServletResponse.setStatus(ErrorCode.getStatusCode(e.getErrorKey()));
        return errorInfo;
    }

}
