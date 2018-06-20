package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
@Valid
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StepDto {

    private String stepId;

    private String caseId;

    @NotEmpty(message = "步骤说明不能为空")
    @ApiModelProperty(value = "步骤说明")
    private String description;

    @ApiModelProperty(value = "条件")
    private String precondition;

    @NotEmpty(message = "步骤类型不能为空")
    @ApiModelProperty(value = "步骤类型")
    private String stepType;

    @NotEmpty(message = "步骤优先级不能为空")
    @ApiModelProperty(value = "步骤优先级")
    private Integer priority;

    @ApiModelProperty(value = "自定义方法")
    private String func;

    @ApiModelProperty(value = "http请求")
    private HttpRequestDto httpRequest;

    @ApiModelProperty(value = "模板ID")
    private String templateId;

    @ApiModelProperty(value = "redis请求信息")
    private RedisInfoDto redisInfo;

    @ApiModelProperty(value = "MySQL请求信息")
    private MySqlInfoDto mysqlInfo;

    @JsonProperty(value = "variables")
    @ApiModelProperty(value = "初始化临时变量")
    private Map<String, String> initTemporaryVariables;

    private String keyInGlobalVariable;
    private String keyInEnvironmentVariable;
    private String keyInTemporaryVariable;
    private String element;

    @ApiModelProperty(value = "校验器")
    private ExtractorDto extractor;

    @ApiModelProperty(value = "校验器")
    private List<VerifierDto> verifiers;

    public void check() {
        int count = 0;
        if (StringUtils.isNotBlank(func)) {
            count++;
        }
        if (httpRequest != null) {
            count++;
        }
        if (StringUtils.isNotBlank(templateId)) {
            count++;
        }
        if (mysqlInfo != null) {
            count++;
        }
        if (redisInfo != null) {
            count++;
        }
        if (count == 0 || count > 1) {
            throw new RuntimeException("每个step有且只能执行一种类型的动作[interfaceId,func,templateId,redis,mysql]");
        }

        if(extractor != null) {
            extractor.check();
        }
    }

}
