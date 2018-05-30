package com.bgw.testing.server.config;

import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.common.ErrorInfo;
import com.bgw.testing.server.util.BaseMDCUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ServerException.class)
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ServerException e) {
        ErrorInfo<String> errorInfo = new ErrorInfo<>();
        errorInfo.setMsg(e.getErrorMsg());
//        ErrorCode.getDescription(e.getErrorKey()) + ":" +
        errorInfo.setCode(e.getErrorKey());
        errorInfo.setCorrelationId(BaseMDCUtils.get());
        BaseMDCUtils.clear();
        httpServletResponse.setStatus(ErrorCode.getStatusCode(e.getErrorKey()));
        return errorInfo;
    }

}
