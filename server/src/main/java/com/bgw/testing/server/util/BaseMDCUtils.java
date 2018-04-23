package com.bgw.testing.server.util;


import org.slf4j.MDC;

public class BaseMDCUtils {

    final static String LOG_STR = "log_Slf4j";

    public static void put(String value) {
        MDC.put(LOG_STR, value);
    }

    public static void put(String key, String value) {
        MDC.put(key, value);
    }

    public static void clear() {
        MDC.clear();
    }

}
