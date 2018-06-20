package com.bgw.testing.server.util;

import com.bgw.testing.common.dto.ExtractorDto;
import com.bgw.testing.server.VariableContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ExtractorUtils {

    private static final Pattern JSON_STRING = Pattern.compile(".*?(\\{.*\\}|\\[.*\\]).*");
    private static final Pattern FILTER = Pattern.compile("([^();]*(\\(([\\s\\S]*?\\)))?)((;(?=([^()]+(\\([\\s\\S]*?\\))?)+))|;?$)");
    private static final Pattern FILTER_FUNC = Pattern.compile("([^(]*)(?:\\((.*)\\))?");

    public static void extract(Object content, ExtractorDto extractorDto, Map<String, String> caseVariable, Map<String, String> stepVariable) {

        VariableContext context = VariableContext.getInstance();

        if (!ConditionUtils.verify(extractorDto.getCondition()) || content == null) {
            return;
        }

        try {
            if (!(content instanceof JsonNode)) {
                String sContent = content.toString().trim();
                if (StringUtils.isBlank(sContent)) {
                    return;
                }

                Matcher matcher = JSON_STRING.matcher(sContent);
                if (matcher.matches()) {
                    sContent = matcher.group(1);
                    ObjectMapper objectMapper = new ObjectMapper();
                    content = objectMapper.readTree(sContent);
                }
            }

            List<Object> extractedContents = extract(content, extractorDto.getElement(), extractorDto.getRegex());

            caseVariable.put("eleCount", extractedContents.size() + "");
            for (int i = 0; i < extractedContents.size(); i++) {
                caseVariable.put("eleIndex", i + "");
                Object extractedContent = extractedContents.get(i);
                String sContent = contentAsText(extractedContent);

                //对结果应用filter
                Object filteredContent = sContent;
                if (StringUtils.isNotBlank(extractorDto.getFilter())) {
                    filteredContent = filter(sContent, extractorDto.getFilter(), caseVariable, stepVariable);
                }
                if (filteredContent == null) {
                    log.error("对内容{}应用filter[{}]失败", sContent, extractorDto.getFilter());
                    continue;
                }

                //获取将要放入context中值
                String valueForContext = extractorDto.getContextValue();
                if (valueForContext == null) {
                    valueForContext = filteredContent.toString();
                }

                //将值放入global上下文
                if (StringUtils.isNotBlank(extractorDto.getKeyInGlobalVariable())) {
                    context.getGlobalVariable().put(extractorDto.getKeyInGlobalVariable(), valueForContext);
                }

                //将值放入Environment上下文
                if (StringUtils.isNotBlank(extractorDto.getKeyInEnvironmentVariable())) {
                    context.getEnvironmentVariable().put(extractorDto.getKeyInEnvironmentVariable(), valueForContext);
                }

                //将值放入CaseVariable上下文
                if (StringUtils.isNotBlank(extractorDto.getKeyInCaseVariable())) {
                    caseVariable.put(extractorDto.getKeyInCaseVariable(), valueForContext);
                }

                //遍历运行子extractors
                for (ExtractorDto extractor : extractorDto.getExtractors()) {
                    extract(filteredContent != null ? filteredContent : extractedContent, extractor, caseVariable, stepVariable);
                }
                caseVariable.remove("eleIndex");
            }
            caseVariable.remove("eleCount");
        } catch (Exception e) {
            throw new RuntimeException("使用解析器[" + BaseJsonUtils.writeValue(extractorDto) + "]解析内容[" + content + "]异常", e);
        }
    }

    private static Object filter(String content, String filter, Map<String, String> caseVariable, Map<String, String> stepVariable) {
        if (StringUtils.isBlank(filter)) {
            return content;
        }

        Object result = content;
        filter = filter.trim();
        Matcher matcher = FILTER.matcher(filter);
        while (matcher.find()) {
            if (StringUtils.isNotBlank(matcher.group(1))) {
                result = filter(matcher.group(1), result, caseVariable, stepVariable);
            }
        }

        return result;
    }

    private static Object filter(String filter, Object content, Map<String, String> caseVariable, Map<String, String> stepVariable) {
        if (content == null) {
            return null;
        }

        Matcher matcher = FILTER_FUNC.matcher(filter);
        if (matcher.matches()) {
            String methodName = matcher.group(1).trim();
            Object[] finalArgs;
            if (matcher.group(2) != null) {
                String[] args = matcher.group(2).split("(?<!\\\\),", -1);
                finalArgs = new Object[args.length + 1];
                finalArgs[0] = content;
                for (int i = 0; i < args.length; i++) {
                    args[i] = args[i].replaceAll("\\\\,", ",");
                }
                System.arraycopy(args, 0, finalArgs, 1, args.length);
            } else {
                finalArgs = new Object[]{content};
            }
            return FuncUtils.getDynamicFuncValue(methodName, finalArgs, caseVariable, stepVariable);
        }

        return content;
    }

    private static List<Object> extract(Object content, String element, String regex) {
        List<Object> contents = new ArrayList<>();
        if (StringUtils.isNotBlank(element)) {
            if (content instanceof JsonNode) {
                extractJson((JsonNode) content, element).forEach(subNode -> contents.add(subNode));
            }
        } else if (StringUtils.isNotBlank(regex)) {
            Matcher matcher = Pattern.compile(regex).matcher(contentAsText(content));
            while (matcher.find()) {
                if (matcher.groupCount() == 0) {
                    contents.add(matcher.group(0));
                } else {
                    contents.add(matcher.group(1));
                }
            }
        } else {
            contents.add(content);
        }

        return contents;
    }

    private static List<JsonNode> extractJson(JsonNode node, String element) {
        List<JsonNode> nodes = Arrays.asList(node);
        if (element.equalsIgnoreCase(".")) {
            if (node instanceof ArrayNode) {
                nodes = new ArrayList<>();
                Iterator<JsonNode> iterator = node.iterator();
                while (iterator.hasNext()) {
                    nodes.add(iterator.next());
                }
            }
        } else {
            String[] subEles = element.split("\\.");
            for (int i = 0; i < subEles.length; i++) {
                boolean parseArray = false;
                if (i == subEles.length - 1) {
                    parseArray = true;
                }
                nodes = extract(nodes, subEles[i], parseArray);
            }
        }
        return nodes;
    }

    /**
     * @param parseArray true-如果element对应的node为arrayNode会将其所有元素做为list返回
     *                   false-如果element对应的node为arrayNode也会直接返回该node
     */
    private static List<JsonNode> extract(List<JsonNode> nodes, String element, boolean parseArray) {
        List<JsonNode> result = new ArrayList<>();
        for (JsonNode node : nodes) {
            if (node instanceof ArrayNode) {
                if (NumberUtils.isNumber(element)) {
                    node = node.get(Integer.valueOf(element));
                } else {
                    Iterator<JsonNode> iterator = node.iterator();
                    while (iterator.hasNext()) {
                        node = iterator.next();
                        node = node.get(element);
                        if (node != null) {
                            result.add(node);
                        }
                    }
                    continue;
                }
            } else {
                node = node.get(element);
            }

            if (node == null) {
                continue;
            }

            if (node instanceof ArrayNode && parseArray) {
                Iterator<JsonNode> iterator = node.iterator();
                while (iterator.hasNext()) {
                    result.add(iterator.next());
                }
            } else {
                result.add(node);
            }
        }
        return result;
    }

    private static String contentAsText(Object content) {
        if (content == null) {
            return "";
        }

        String result = BaseJsonUtils.writeValue(content);
        if (content instanceof JsonNode) {
            if (content instanceof TextNode) {
                result = ((TextNode) content).asText();
            } else {
                result = content.toString();
            }
        }

        return result.replaceAll("[\\s\\u00A0]+$", "").replaceAll("^[\\s\\u00A0]+", "");
    }

}
