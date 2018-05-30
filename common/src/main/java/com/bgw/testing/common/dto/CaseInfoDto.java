package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Valid
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CaseInfoDto {

    @ApiModelProperty(value = "用例ID")
    private String caseId;
    @ApiModelProperty(value = "组ID")
    private String groupId;
    @ApiModelProperty(value = "用例名称")
    private String caseName;
    @ApiModelProperty(value = "用例说明")
    private String description;
    @NotNull
    @ApiModelProperty(value = "用例优先级")
    private Integer priority;
    @ApiModelProperty(value = "用例内容")
    private CaseContentDto caseContent;
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

}
