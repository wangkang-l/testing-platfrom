package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StepReportDto {
    private String stepResultId;

    private String description;

    private String stepType;

    private Boolean result = true;

    private Date startTime;

    private Date endTime;

    private String stepContent;

    private String requestContent;

    private String responseContent;

    private String errorInfo;

    public StepReportDto() {}

    public StepReportDto(String description, boolean success, String errorInfo) {
        this.description = description;
        this.result = success;
        this.errorInfo = errorInfo;
        this.endTime = new Date();
    }
}
