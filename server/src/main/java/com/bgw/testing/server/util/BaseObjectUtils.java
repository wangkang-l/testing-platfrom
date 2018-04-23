package com.bgw.testing.server.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseObjectUtils {

    private static final List<Class<?>> PRIMITIVE = new ArrayList();

    public static boolean isPrimitive(Type type) {
        return type instanceof ParameterizedType ? isPrimitive(((ParameterizedType)type).getActualTypeArguments()[0]) : PRIMITIVE.contains(type);
    }

    static {
        PRIMITIVE.add(Character.class);
        PRIMITIVE.add(String.class);
        PRIMITIVE.add(Boolean.class);
        PRIMITIVE.add(Byte.class);
        PRIMITIVE.add(Short.class);
        PRIMITIVE.add(Integer.class);
        PRIMITIVE.add(Long.class);
        PRIMITIVE.add(Float.class);
        PRIMITIVE.add(Double.class);
        PRIMITIVE.add(BigDecimal.class);
        PRIMITIVE.add(Date.class);
        PRIMITIVE.add(Boolean.TYPE);
        PRIMITIVE.add(Byte.TYPE);
        PRIMITIVE.add(Short.TYPE);
        PRIMITIVE.add(Integer.TYPE);
        PRIMITIVE.add(Long.TYPE);
        PRIMITIVE.add(Float.TYPE);
        PRIMITIVE.add(Double.TYPE);
        PRIMITIVE.add(Character.TYPE);
    }
}
