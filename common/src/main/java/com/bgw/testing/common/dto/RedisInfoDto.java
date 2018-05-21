package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class RedisInfoDto {

    @NotBlank(message = "redis数据库不能为空")
    private Integer dbIndex;
    @NotBlank(message = "key不能为空")
    private String key;
    private String value;
    private Integer expire;
    private String action;

}