package com.bgw.testing.server.util;

import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.server.VariableContext;
import com.bgw.testing.server.config.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ContextUtils {

    private static final Pattern PLACE_HOLDER = Pattern.compile("\\$\\s*\\{(.*?)\\}");

    public static Map<String, String> fillValue(Map<String, String> initContent, Map<String, String> caseVariable, Map<String, String> stepVariable) {
        if (initContent != null && initContent.size() > 0) {
            initContent.keySet().forEach(key -> {
                initContent.put(key, fillValue(initContent.get(key), caseVariable, stepVariable));
            });
        }
        return initContent;
    }

    public static String fillValue(String var3, Map<String, String> caseVariable, Map<String, String> stepVariable) {
        if (StringUtils.isNotBlank(var3)) {
            List<String> placeHolders  = splitPlaceHolder(var3);
            for (String placeHolder : placeHolders) {
                String realValue = getContextValue(placeHolder, caseVariable, stepVariable);
                log.info("占位符:{},实际值:{}", placeHolder, realValue);
                var3 = var3.replaceAll("\\$\\{" + placeHolder + "\\}", realValue);
            }
        }
        return var3;
    }

    private static List<String> splitPlaceHolder(String var4) {
        List<String> placeHolders = new ArrayList<>();
        Matcher matcher = PLACE_HOLDER.matcher(var4);
        while (matcher.find()) {
            placeHolders.add(matcher.group(1));
        }
        return placeHolders;
    }

    private static String getContextValue(String placeHolder, Map<String, String> caseVariable, Map<String, String> stepVariable) {
        VariableContext context = VariableContext.getInstance();
        if (stepVariable != null && stepVariable.containsKey(placeHolder)) {
            log.info("占位符:{}使用StepVariable中的值", placeHolder);
            return stepVariable.get(placeHolder);
        } else if (caseVariable != null && caseVariable.containsKey(placeHolder)) {
            log.info("占位符:{}使用CaseVariable中的值", placeHolder);
            return caseVariable.get(placeHolder);
        } else if (context.getEnvironmentVariable().containsKey(placeHolder)) {
            log.info("占位符:{}使用EnvironmentVariable中的值", placeHolder);
            return context.getEnvironmentVariable().get(placeHolder);
        } else if (context.getGlobalVariable().containsKey(placeHolder)) {
            log.info("占位符:{}使用GlobalVariable中的值", placeHolder);
            return context.getGlobalVariable().get(placeHolder);
        } else {
            throw new ServerException(ErrorCode.NOT_EXIST, "变量：" + placeHolder);
        }
    }

}
