package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CaseReportDto {
    private String caseResultId;

    private String caseId;

    private String caseName;

    private Boolean result;

    private String traceLogId;

    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private List<StepReportDto> stepReportDtoList;

    public CaseReportDto() {}

    public CaseReportDto(String caseId, String caseName) {
        this.caseName = caseName;
        this.caseId = caseId;
        this.startTime = new Date();
        this.result = false;
    }

}
