package com.bgw.testing.server.util;


import com.netflix.astyanax.util.TimeUUIDUtils;
import org.apache.commons.lang3.StringUtils;

public class BaseStringUtils {

    public static String uuid() {
        return TimeUUIDUtils.getUniqueTimeUUIDinMillis().toString();
    }

    public static String uuidSimple() {
        return uuid().replaceAll("-", "");
    }

    /**
     * 首字母大写
     * @param name
     * @return
     */
    public static String initialCapital(String name) {
        if (StringUtils.isNotBlank(name)) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
//            char[] cs=name.toCharArray();
//            cs[0]-=32;
//            return String.valueOf(cs);
        }
        return name;
    }

}
