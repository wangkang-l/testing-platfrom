package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class MySqlInfoDto {

    @ApiModelProperty(name = "默认库bgw_automation,其他库需要添加库名")
    private String dbName;
    @NotEmpty(message = "sql语句不能为空")
    private String sql;
    private boolean isQuery;
    private boolean isSingleResult;
    private List<String> params;

}
