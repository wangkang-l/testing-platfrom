package com.bgw.testing.server.util;

import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.server.config.ServerException;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ConditionUtils {

    public static boolean verify(String condition) {
        if (StringUtils.isBlank(condition)) {
            return true;
        }
        return groovyShell(condition);
    }

    private static boolean groovyShell(String script) {
        try {
            GroovyShell groovyShell = new GroovyShell();
            Object result =  groovyShell.evaluate(script.replace("$", "\\$"));
            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ServerException(ErrorCode.INVALID_CONDITION, script);
        }
    }

}