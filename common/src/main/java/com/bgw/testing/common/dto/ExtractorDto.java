package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.web.client.ResponseExtractor;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ExtractorDto {

    private String condition;
    private String element;
    private String regex;
    private String filter; // 对解析结果进行加工处理, 可有多个规则, 以;号隔开. e.g. parseDate(yyyyMMdd)
    private String keyInGlobalVariable;
    private String keyInEnvironmentVariable;
    private String keyInCaseVariable;
    private String contextValue;
    private List<ResponseExtractor> extractors = new ArrayList<>();

}
