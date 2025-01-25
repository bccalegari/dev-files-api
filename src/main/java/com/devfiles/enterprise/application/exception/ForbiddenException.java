package com.devfiles.enterprise.application.exception;

import com.devfiles.enterprise.domain.constant.ErrorCode;

import java.io.Serial;

public class ForbiddenException extends CoreException {
    @Serial
    private static final long serialVersionUID = -580790652457675403L;
    private static final ErrorCode errorCode = ErrorCode.FORBIDDEN;

    public ForbiddenException(String message) {
        super(message, errorCode.getHttpStatus(), errorCode);
    }

    public ForbiddenException() {
        super("Forbidden", errorCode.getHttpStatus(), errorCode);
    }
}