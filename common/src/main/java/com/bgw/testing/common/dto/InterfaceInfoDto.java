package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InterfaceInfoDto {
    private String interfaceId;
    private String description;
    private String path;
    private String method;
    private String body;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private List<InterfaceParamDto> interfaceParamDtoList;
}
