package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class MySqlInfoDto {

    @NotBlank(message = "dao属性不能为空")
    private String dao;
    @NotBlank(message = "sql语句不能为空")
    private String sql;
    private Boolean isQuery;
    private Boolean isSingleResult;
    private List<String> params;

}
