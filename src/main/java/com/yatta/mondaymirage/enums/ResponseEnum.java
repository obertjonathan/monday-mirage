package com.yatta.mondaymirage.enums;

import com.yatta.mondaymirage.constant.MessageConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ResponseEnum {

    SUCCESS(MessageConstant.SUCCESS, "success", "Success", HttpStatus.OK),
    NOT_FOUND(MessageConstant.NOT_FOUND, MessageConstant.NOT_FOUND, "Request or data not found ", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_PERMITTED(MessageConstant.API_KEY_INVALID, MessageConstant.NOT_AUTHORIZE, "Permission is not authorized", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String type;
    private final String message;
    private final HttpStatus httpStatus;
}
