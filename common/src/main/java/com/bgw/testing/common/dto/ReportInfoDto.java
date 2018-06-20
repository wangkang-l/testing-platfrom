package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReportInfoDto {
    private String batchNo;
    private String taskId;
    private Date startTime;
    private Date endTime;
    private Integer totalNum;
    private Integer succNum;
    private Integer failNum;
    private String passRate;
    private String executor;
    private List<CaseReportDto> caseReportDtoList;

    public void passRate() {
        Double d = succNum == 0 ? 0.00 : Double.valueOf(new DecimalFormat("0.00").format(this.succNum/(this.totalNum/1.0)));
        this.passRate = (int) (d * 100) + "%";
    }
}
