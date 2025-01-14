package com.devfiles.enterprise.application.exception;

import com.devfiles.enterprise.domain.constant.ErrorCode;

import java.io.Serial;

public class AlreadyExistsException extends CoreException {
    @Serial
    private static final long serialVersionUID = -5790354890770000847L;
    private static final ErrorCode errorCode = ErrorCode.ALREADY_EXISTS;

    public AlreadyExistsException(String message) {
        super(message, errorCode.getHttpStatus(), errorCode);
    }

    public AlreadyExistsException() {
        super("Already exists", errorCode.getHttpStatus(), errorCode);
    }
}