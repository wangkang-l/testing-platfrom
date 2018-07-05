package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.*;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InterfaceInfoDto {
    @ApiModelProperty(value = "接口ID：新增为空，修改必填")
    private String interfaceId;

    @NotEmpty(message = "组ID不能为空")
    @ApiModelProperty(value = "组ID")
    private String groupId;

    @ApiModelProperty(value = "接口描述")
    private String name;

    @ApiModelProperty(value = "接口描述")
    private String description;

    @NotEmpty(message = "接口路径不能为空")
    @ApiModelProperty(value = "接口路径")
    private String path;

    @NotEmpty(message = "请求方式不能为空")
    @ApiModelProperty(value = "请求方式")
    private String method;

    private String body;
    private List<InterfaceParamDto> querys = new ArrayList<>();
    private List<InterfaceParamDto> headers = new ArrayList<>();

    private String interfacePath;
}
