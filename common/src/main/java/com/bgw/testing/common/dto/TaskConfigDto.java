package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TaskConfigDto {

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @NotEmpty(message = "任务名称不能为空")
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @NotEmpty(message = "环境ID不能为空")
    @ApiModelProperty(value = "环境")
    private String environmentId;

    @NotEmpty(message = "cron表达式不能为空")
    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "运行方式")
    private String runMethod;

    @ApiModelProperty(value = "任务状态")
    private String taskStatus;

    @ApiModelProperty(value = "任务参数")
    private List<TaskPropertyDto> taskProperties = new ArrayList<>();

    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
}
