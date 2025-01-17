package com.devfiles.enterprise.application.exception;

import com.devfiles.enterprise.domain.constant.ErrorCode;

import java.io.Serial;

public class UnauthorizedException extends CoreException {
    @Serial
    private static final long serialVersionUID = -1318166409945463410L;
    private static final ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    public UnauthorizedException(String message) {
        super(message, errorCode.getHttpStatus(), errorCode);
    }

    public UnauthorizedException() {
        super("Unauthorized", errorCode.getHttpStatus(), errorCode);
    }
}