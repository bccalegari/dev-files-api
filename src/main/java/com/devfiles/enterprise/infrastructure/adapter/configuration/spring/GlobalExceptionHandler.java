package com.devfiles.enterprise.infrastructure.adapter.configuration.spring;

import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import com.devfiles.enterprise.application.exception.CoreException;
import com.devfiles.enterprise.domain.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Object>> handleGenericException(Exception exception) {
        log.error(exception.getMessage(), exception);
        var responseDto = ResponseDto.error(ErrorCode.INTERNAL_SERVER_ERROR, CoreException.DEFAULT_MESSAGE);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(responseDto);
    }

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ResponseDto<Object>> handleCoreException(CoreException exception) {
        log.error(exception.getMessage(), exception);
        var responseDto = ResponseDto.error(exception.getErrorCode(), exception.getMessage());
        return ResponseEntity.status(exception.getErrorCode().getHttpStatus()).body(responseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Object>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        log.error("Validation failed for argument: {}", exception.getMessage(), exception);
        var errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("Field: %s, Message: %s", error.getField(), error.getDefaultMessage()))
                .toList();
        var responseDto = ResponseDto.error(ErrorCode.BAD_REQUEST, errors.toString());
        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getHttpStatus()).body(responseDto);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ResponseDto<Object>> handleHandlerMethodValidationExceptions(HandlerMethodValidationException exception) {
        log.error("Validation failed in method handler: {}", exception.getMessage(), exception);
        var errors = exception.getAllErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();
        var responseDto = ResponseDto.error(ErrorCode.BAD_REQUEST, errors.toString());
        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getHttpStatus()).body(responseDto);
    }
}