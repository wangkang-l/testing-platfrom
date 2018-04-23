package com.bgw.testing.server.util;


import com.netflix.astyanax.util.TimeUUIDUtils;

public class BaseStringUtils {

    public static String uuid() {
        return TimeUUIDUtils.getUniqueTimeUUIDinMillis().toString();
    }

    public static String uuidSimple() {
        return uuid().replaceAll("-", "");
    }

}
