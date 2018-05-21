package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApiRequestDto {

    private String interfaceId;
    private String description;
    private String method;
    private String url;
    private Map<String, String> query = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private Integer expectedStatusCode;

}
