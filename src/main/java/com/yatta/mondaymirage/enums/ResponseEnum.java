package com.yatta.mondaymirage.enums;

import com.yatta.mondaymirage.constant.MessageConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseEnum {

    SUCCESS(MessageConstant.SUCCESS, "success", "Success", HttpStatus.OK),
    NOT_FOUND(MessageConstant.NOT_FOUND, MessageConstant.NOT_FOUND, "Request or data not found ", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_PERMITTED(MessageConstant.API_KEY_INVALID, MessageConstant.NOT_AUTHORIZE, "Permission is not authorized", HttpStatus.UNAUTHORIZED),
    INVALID_PARAMETER(MessageConstant.INVALID_PARAMETER, MessageConstant.REQUEST_ERROR, "Invalid parameter", HttpStatus.BAD_REQUEST),
    UNKNOWN(MessageConstant.UNKNOWN, MessageConstant.UNKNOWN, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String type;
    private final String message;
    private final HttpStatus httpStatus;
}
