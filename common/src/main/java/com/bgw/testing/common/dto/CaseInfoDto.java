package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Valid
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CaseInfoDto {

    @ApiModelProperty(value = "用例ID")
    private String caseId;

    @NotEmpty(message = "组ID不能为空")
    @ApiModelProperty(value = "组ID")
    private String groupId;

    @NotEmpty(message = "用例名称不能为空")
    @ApiModelProperty(value = "用例名称")
    private String caseName;

    @ApiModelProperty(value = "用例路径")
    private String casePath;

    @NotEmpty(message = "用例说明不能为空")
    @ApiModelProperty(value = "用例说明")
    private String description;

    @NotNull(message = "用例优先级不能为空")
    @ApiModelProperty(value = "用例优先级")
    private Integer priority;

    @ApiModelProperty(value = "前置条件")
    private String precondition;

    @JsonProperty(value = "variables")
    @ApiModelProperty(value = "初始化临时变量")
    private Map<String, String> initTemporaryVariables;

    @ApiModelProperty(value = "是否可继承，默认FALSE")
    private boolean inherit = false;

    @ApiModelProperty(value = "用例步骤")
    private List<StepDto> steps;

    @ApiModelProperty(value = "用例状态")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /**
     * 校验用例格式与必填字段
     */
    public void check() {
        steps.forEach(StepDto::check);
    }

}
