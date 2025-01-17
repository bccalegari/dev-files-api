package com.devfiles.core.user.application.exception;

import com.devfiles.enterprise.application.exception.BadRequestException;

import java.io.Serial;

public class UserInvalidCredentialsException extends BadRequestException {
    @Serial
    private static final long serialVersionUID = 1660797043891763621L;

    public UserInvalidCredentialsException() {
        super("Invalid credentials");
    }
}