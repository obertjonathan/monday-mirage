package com.yatta.mondaymirage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yatta.mondaymirage.enums.ResponseEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ErrorBodyResponse implements BodyResponse {
    private BaseResponse error;

    public ErrorBodyResponse(ResponseEnum responseEnum) {
        this.error = new BaseResponse(responseEnum);
    }

    public ErrorBodyResponse(ResponseEnum responseEnum, String message) {
        this(responseEnum);
        this.error.setMessage(message);
    }
}
