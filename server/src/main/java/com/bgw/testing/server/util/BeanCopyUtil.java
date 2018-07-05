package com.bgw.testing.server.util;

import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class BeanCopyUtil {

    private static DozerBeanMapper dozer = new DozerBeanMapper();

    public BeanCopyUtil() {
    }

    public static <T> T objConvert(Object obj, Class<T> toObj) {
        return null == obj ? null : dozer.map(obj, toObj);
    }

    public static void copy(Object source, Object toObj) {
        if (null != source) {
            dozer.map(source, toObj);
        }

    }

    public static <T> List<T> convertList(Collection<?> collection, Class<T> toObj) {
        if (collection == null) {
            return null;
        } else if (toObj == null) {
            return null;
        } else {
            List<T> list = new ArrayList();
            Iterator var3 = collection.iterator();

            while(var3.hasNext()) {
                Object obj = var3.next();
                list.add(dozer.map(obj, toObj));
            }

            return list;
        }
    }

}
