package com.devfiles.enterprise.application.exception;

import com.devfiles.enterprise.domain.constant.ErrorCode;

import java.io.Serial;

public class BadRequestException extends CoreException {
    @Serial
    private static final long serialVersionUID = -2879778659962911173L;
    private static final ErrorCode errorCode = ErrorCode.BAD_REQUEST;

    public BadRequestException(String message) {
        super(message, errorCode.getHttpStatus(), errorCode);
    }

    public BadRequestException() {
        super("Bad request", errorCode.getHttpStatus(), errorCode);
    }
}