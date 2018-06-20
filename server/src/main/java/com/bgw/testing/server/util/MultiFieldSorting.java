package com.bgw.testing.server.util;

import java.lang.reflect.Method;
import java.text.Collator;
import java.util.Comparator;

public class MultiFieldSorting implements Comparator<Object> {

    //排序字段
    String[] fields = null;
    //字段对应的排序规则，升序asc 或 降序desc
    String[] orders = null;

    boolean isValue;

    Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

    public MultiFieldSorting() {
        super();
    }

    public MultiFieldSorting(String[] fields, String[] orders) {
        super();
        this.fields = fields;
        this.orders = orders;
    }

    public String[] getFields_user() {
        return fields;
    }

    public void setFields(String[] fields_user) {
        this.fields = fields_user;
    }

    public String[] getOrders() {
        return orders;
    }

    public void setOrders(String[] orders) {
        this.orders = orders;
    }

    public int compare(Object obj1, Object obj2) {
        // 没有属性，则不排序
        if (fields == null || fields.length <= 0) {
            return 2;// 不比较
        }
        for (int i = 0; i < fields.length;i++ ) {
            int ret=compareField(obj1, obj2, fields[i]);
            if (ret==0) {
                continue;
            }
            if (ret!=0) {
                if ("asc".equalsIgnoreCase(orders[i])) {
                    return ret;
                }else if("desc".equalsIgnoreCase(orders[i])) {
                    return ret*-1;
                }
            }else {
                continue;
            }
        }
        return 0;
    }

    private int compareField(Object o1, Object o2, String fieldName) {
        Object value1=getFieldValueByName(fieldName, o1);
        Object value2=getFieldValueByName(fieldName, o2);
        if (value1==null ||value2==null) {
            if (value1!=null) {
                return 1;
            }if (value2!=null) {
                return -1;
            }else {
                return 0;
            }
        }
        if (isValue) {
            double v1 = ((Number)value1).doubleValue();
            double v2 = ((Number)value2).doubleValue();
            if (v1-v2 > 0) {
                return 1;
            }else if (v1==v2) {
                return 0;
            }
            return -1;
        } else {
            String v1 = value1.toString();
            String v2 = value2.toString();
            int i= cmp.compare(v1, v2);
            if (i>0) {
                return 1;
            }else if (i==0) {
                return 0;
            }
            return -1;
        }
    }

    private Object getFieldValueByName(String fieldName, Object obj) {
        try {
            Class<? extends Object> objClass = obj.getClass();
            String Letter = fieldName.substring(0, 1).toUpperCase();
            String methodStr = "get" + Letter + fieldName.substring(1);

            Method method = objClass.getMethod(methodStr, new Class[] {});
            Object value = method.invoke(obj, new Object[] {});

            if (value instanceof Number) {
                this.setValue(true);
            } else {
                this.setValue(false);
            }

            return value;
        } catch (Exception e) {
            System.err.println("FieldName:" + fieldName + " is not exsited.");
            return null;
        }
    }

    public boolean isValue() {
        return isValue;
    }

    public void setValue(boolean isValue) {
        this.isValue = isValue;
    }

}
