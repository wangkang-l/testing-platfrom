package com.bgw.testing.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DictionaryDto {

    /**
     * dictNo : string
     * dictType : string
     * dictValue : string
     * id : string
     * orderNo : 0
     * remark : string
     */

    private String dictNo;
    private String dictType;
    private String dictValue;
    private String dictId;
    private int order;
    private String remark;
}
