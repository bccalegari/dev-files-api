package com.devfiles.core.user.invitation.application.exception;

import com.devfiles.enterprise.application.exception.BadRequestException;

import java.io.Serial;

public class InvalidActivationCodeException extends BadRequestException {
    @Serial
    private static final long serialVersionUID = -1563413707471171158L;

    public InvalidActivationCodeException() {
        super("Invalid activation code");
    }
}