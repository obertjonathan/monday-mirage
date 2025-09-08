package com.yatta.mondaymirage.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponseException extends RuntimeException {
    private final String responseBody;
    private final HttpStatus httpStatus;
}
