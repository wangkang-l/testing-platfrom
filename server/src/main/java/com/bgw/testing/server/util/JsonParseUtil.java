package com.bgw.testing.server.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Iterator;

public class JsonParseUtil {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 根据传入key名称，获取value
     * @param strJson, @param strkey
     * @return Object
     */
    public Object getValueByKey(@Param(value = "strJson") Object strJson, @Param(value = "strKey") String strKey) throws Exception {
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
