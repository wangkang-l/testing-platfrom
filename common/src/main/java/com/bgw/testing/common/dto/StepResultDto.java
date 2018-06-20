package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.http.HttpHeaders;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StepResultDto {

    private boolean success = true;
    private String data;
    private Throwable exception;
    private HttpHeaders headers;
    private int statusCode;

    public StepResultDto(){}

    public StepResultDto(String data) {
        this.data = data;
    }

    public StepResultDto(boolean success, String data, Throwable e) {
        this.success = success;
        this.data = data;
        this.exception = e;
    }

}
