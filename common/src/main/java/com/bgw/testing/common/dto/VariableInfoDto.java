package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VariableInfoDto {

    @ApiModelProperty(value = "变量ID")
    private String variableId;
    @NotNull
    @ApiModelProperty(value = "变量类型")
    private String type;
    @ApiModelProperty(value = "环境ID")
    private String environmentId;
    @NotNull
    @ApiModelProperty(value = "key")
    private String configKey;
    @ApiModelProperty(value = "value")
    private String configValue;
    @ApiModelProperty(value = "备注")
    private String remark;

}
