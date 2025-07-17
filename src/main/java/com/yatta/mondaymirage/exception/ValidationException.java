package com.yatta.mondaymirage.exception;

import com.yatta.mondaymirage.enums.ResponseEnum;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final ResponseEnum responseEnum;

    public ValidationException(ResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.responseEnum = responseEnum;
    }

    public ValidationException(ResponseEnum responseEnum, String message) {
        super(message);
        this.responseEnum = responseEnum;
    }
}
