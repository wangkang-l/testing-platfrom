package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TaskPropertyDto {

    @ApiModelProperty(value = "用例组")
    private String groupId;
    @ApiModelProperty(value = "次序，数字越小越先执行")
    private Integer priority;
    @ApiModelProperty(value = "用例ID")
    private String caseIds[];

}
