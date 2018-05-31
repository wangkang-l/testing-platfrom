package com.bgw.testing.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VerifierDto {

    @NotBlank(message = "verifier中condition不能为空")
    private String condition;
    private String msg;

}
