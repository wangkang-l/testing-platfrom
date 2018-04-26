package com.bgw.testing.server.config;

import com.bgw.testing.common.ErrorCode;
import com.bgw.testing.common.ErrorInfo;
import com.bgw.testing.server.util.BaseMDCUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ServerException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletResponse res, ServerException e) throws Exception {
        ErrorInfo<String> errorInfo = new ErrorInfo<>();
        errorInfo.setMsg(ErrorCode.getDescription(e.getErrorKey()) + ":" + e.getErrorMsg());
        errorInfo.setCode(e.getErrorKey());
        errorInfo.setCorrelationId(BaseMDCUtils.get());
        BaseMDCUtils.clear();

        res.setStatus(ErrorCode.getStatusCode(e.getErrorKey()));

        return errorInfo;
    }

}
