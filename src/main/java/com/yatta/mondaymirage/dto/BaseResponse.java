package com.yatta.mondaymirage.dto;

import com.yatta.mondaymirage.enums.ResponseEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class BaseResponse implements Serializable {
    private String code;
    private String type;
    private String message;

    public BaseResponse(ResponseEnum responseEnum) {
        this.setCode(responseEnum.getCode());
        this.setType(responseEnum.getType());
        this.setMessage(responseEnum.getMessage());
    }
}
