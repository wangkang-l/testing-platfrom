package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InterfaceParamDto {
    private String InterfaceParamId;
    private String interfaceId;
    private String parameterType;
    private String key;
    private String value;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
}
