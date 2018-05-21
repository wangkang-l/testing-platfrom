package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CaseBasicDto {

    private String caseId;
    private String groupId;
    private String caseName;
    private String description;
    private int priority;
    private CaseContentDto caseContent;
    private boolean isInvalid;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;

}
