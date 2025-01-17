package com.devfiles.enterprise.application.exception;

import com.devfiles.enterprise.domain.constant.ErrorCode;

import java.io.Serial;

public class NotFoundException extends CoreException {
    @Serial
    private static final long serialVersionUID = 5771069616133309816L;
    private static final ErrorCode errorCode = ErrorCode.NOT_FOUND;

    public NotFoundException(String message) {
        super(message, errorCode.getHttpStatus(), errorCode);
    }

    public NotFoundException() {
        super("Not found", errorCode.getHttpStatus(), errorCode);
    }
}