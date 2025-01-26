package com.devfiles.core.user.application.exception;

import com.devfiles.enterprise.application.exception.BadRequestException;

import java.io.Serial;

public class UserAlreadyActivatedException extends BadRequestException {
    @Serial
    private static final long serialVersionUID = -9163152147837762164L;

    public UserAlreadyActivatedException() {
        super("User already activated");
    }
}