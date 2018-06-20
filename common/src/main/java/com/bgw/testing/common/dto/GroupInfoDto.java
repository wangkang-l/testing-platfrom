package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GroupInfoDto {

    @ApiModelProperty(value = "组ID：新增为空，修改必填")
    private String groupId;

    @NotEmpty(message = "组名称不能为空")
    @ApiModelProperty(value = "组名称")
    private String groupName;

    @NotEmpty(message = "父ID不能为空")
    @ApiModelProperty(value = "父ID")
    private String parentId;

    private int priority;
    private Boolean isFinally = true;
    private String path;

}