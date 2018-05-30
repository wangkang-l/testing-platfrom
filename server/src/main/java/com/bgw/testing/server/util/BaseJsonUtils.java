package com.bgw.testing.server.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Preconditions;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public class BaseJsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        mapper.registerModule(new JavaTimeModule());
    }

    public static <T> T readValue(String s, Class<T> cls) {
        Preconditions.checkArgument(cls != null, "Class类型不能为空");
        if (cls == String.class) {
            return (T) s;
        } else if (StringUtils.isBlank(s)) {
            return null;
        } else {
            try {
                return mapper.readValue(s, cls);
            } catch (Throwable var4) {
                log.error("无法将{}转换为类型为[{}]的对象: {}", new Object[]{s, cls.getSimpleName(), ExceptionUtils.getStackTrace(var4)});
                return null;
            }
        }
    }

    public static <T> T readValueChecked(String s, Class<T> cls) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(s), "字串不能为空");
        Preconditions.checkArgument(cls != null, "Class类型不能为空");
        if (cls == String.class) {
            return (T) s;
        } else {
            return mapper.readValue(s, cls);
        }
    }

    public static <T> T readValue(String s, TypeReference typeReference) {
        Preconditions.checkArgument(StringUtils.isNotBlank(s), "字串不能为空");
        Preconditions.checkArgument(typeReference != null, "TypeWrapper类型不能为空");

        try {
            return mapper.readValue(s, typeReference);
        } catch (Throwable var4) {
            log.error("无法将{}转换为类型为[{}]的对象: {}", new Object[]{s, typeReference.getType(), ExceptionUtils.getStackTrace(var4)});
            return null;
        }
    }

    public static <T> T readValue(Map<String, Object> map, Class<T> cls) {
        Preconditions.checkArgument(map != null, "map不能为null");
        Preconditions.checkArgument(cls != null, "Class类型不能为空");
        if (cls == String.class) {
            return (T) writeValue(map);
        } else {
            try {
                return mapper.convertValue(map, cls);
            } catch (Throwable var4) {
                log.error("无法将Map{}转换为类型为[{}]的对象: {}", new Object[]{map, cls.getSimpleName(), ExceptionUtils.getStackTrace(var4)});
                return null;
            }
        }
    }

    public static <T> List<T> readValues(String s, Class<T> cls) {
        Preconditions.checkArgument(StringUtils.isNotBlank(s), "字串不能为空");
        Preconditions.checkArgument(cls != null, "Class类型不能为空");

        List list;
        try {
            list = (List)mapper.readValue(s, List.class);
        } catch (Throwable var5) {
            log.error("无法将{}转换为数组对象: {}", s, ExceptionUtils.getStackTrace(var5));
            return Collections.emptyList();
        }

        return cls == List.class ? list : (List)list.stream().map((ele) -> {
            return ele instanceof Map ? readValue((Map)ele, cls) : readValue(ele.toString(), cls);
        }).collect(Collectors.toList());
    }

    public static String writeValue(Object obj) {
        if (obj == null) {
            return null;
        } else if (BaseObjectUtils.isPrimitive(obj.getClass())) {
            return obj.toString();
        } else {
            try {
                return mapper.writeValueAsString(obj);
            } catch (Throwable var3) {
                log.error("json转换异常", var3);
                return null;
            }
        }
    }


    /**
     * JSON格式化输出
     * @param uglyJSONString
     * @return
     */
    public static String jsonFormatter(String uglyJSONString){
        if (maybeJson(uglyJSONString)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(uglyJSONString);
            return gson.toJson(je);
        }
        return uglyJSONString;
    }

    /**
     * 是否为JSON校验
     * @param json
     * @return
     */
    public static boolean maybeJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    /**
     * 根据传入key名称，获取value
     * @param strJson, @param strkey
     * @return Object
     */
    public Object getValueByKey(Object strJson, String strKey) throws Exception {
        Object rvalue=parseJson(strJson, strKey);
        if(rvalue==""){
            rvalue = "key不存在";
        }
        return rvalue;
    }

    private Object parseJson(Object strJson, String strKey)throws Exception{
        JsonNode jsonNode =  mapper.readTree(strJson.toString());
        Iterator<String> keys = jsonNode.fieldNames();
        Object keyValue = "";
        while(keys.hasNext()) {
            String fieldName = keys.next();
            Object json_value = jsonNode.get(fieldName);
            if (fieldName.equals(strKey)) {
                keyValue = json_value;
                if(keyValue==""){
                    keyValue = "value为空值";
                }
                break;
            }
            else if (json_value.toString().substring(0,1).contains("{")){
                keyValue=parseJson(json_value,strKey);
            }
        }
        return keyValue;
    }
}
