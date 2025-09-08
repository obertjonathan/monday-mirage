package com.yatta.mondaymirage.exception.handler;

import com.yatta.mondaymirage.dto.ErrorBodyResponse;
import com.yatta.mondaymirage.enums.ResponseEnum;
import com.yatta.mondaymirage.exception.ErrorResponseException;
import com.yatta.mondaymirage.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<String> handleErrorResponseException(ErrorResponseException e) {
        log.warn("ErrorResponseException: {}", e.getMessage());

        return ResponseEntity.status(e.getHttpStatus())
                .body(e.getResponseBody());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorBodyResponse> handleValidationException(ValidationException e) {
        log.warn("ValidationException: {}", e.getMessage());

        return ResponseEntity.status(e.getResponseEnum().getHttpStatus())
                .body(new ErrorBodyResponse(e.getResponseEnum(), e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorBodyResponse> handlerPostgresException(ConstraintViolationException e) {
        log.warn("org.hibernate.exception.ConstraintViolationException: {}", e.getMessage());
        log.warn("SQL State : {} ", e.getSQLException().getSQLState());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorBodyResponse(ResponseEnum.UNKNOWN));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorBodyResponse> handleBindException(BindException ex) {
        log.warn("Handle BindException ...", ex);

        String message = getSingleErrorMessage(ex.getFieldErrors(), ex.getGlobalErrors());
        var response = new ErrorBodyResponse(ResponseEnum.INVALID_PARAMETER, message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorBodyResponse> handleUnknownException(Exception ex) {
        log.warn("Handle Unknown Exception: {} ", ex.getMessage());

        var response = new ErrorBodyResponse(ResponseEnum.UNKNOWN);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorBodyResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException caught: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorBodyResponse(ResponseEnum.UNKNOWN));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBodyResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var message = getSingleErrorMessage(
                e.getBindingResult().getFieldErrors(),
                e.getBindingResult().getGlobalErrors()
        );
        var response = new ErrorBodyResponse(ResponseEnum.INVALID_PARAMETER, message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    private static String getSingleErrorMessage(List<FieldError> errorFields, List<ObjectError> getGlobalErrors) {
        return Optional.ofNullable(getErrorFieldsMessage(errorFields))
                .orElse(getGlobalErrorsMessage(getGlobalErrors));
    }

    private static String getErrorFieldsMessage(List<FieldError> errorFields) {
        return Optional.ofNullable(errorFields)
                .filter(Predicate.not(CollectionUtils::isEmpty))
                .flatMap(listErrorFields -> listErrorFields.stream()
                        .findFirst()
                        .map(field -> field.getField() + " " + field.getDefaultMessage()))
                .orElse(null);
    }

    private static String getGlobalErrorsMessage(List<ObjectError> globalErrors) {
        return Optional.ofNullable(globalErrors)
                .filter(Predicate.not(CollectionUtils::isEmpty))
                .flatMap(listGlobalErrors -> listGlobalErrors.stream()
                        .findFirst()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage))
                .orElse(null);
    }
}
