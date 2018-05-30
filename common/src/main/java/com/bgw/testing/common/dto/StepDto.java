package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StepDto {

    private String description;
    private String condition;
    private String stepType;
    private String func;
    private String intefaceId;
    private String templateId;
    private RedisInfoDto redisInfo;
    private MySqlInfoDto mysqlInfo;
    private String keyInGlobalVariable;
    private String keyInEnvironmentVariable;
    private String keyInCaseVariable;
    private String element;
    private String extractor;
    private String verifier;

}
