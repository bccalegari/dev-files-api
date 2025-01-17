package com.devfiles.core.user.application.exception;

import com.devfiles.enterprise.application.exception.NotFoundException;

import java.io.Serial;

public class UserNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = -6550672187710749329L;

    public UserNotFoundException() {
        super("User not found");
    }
}