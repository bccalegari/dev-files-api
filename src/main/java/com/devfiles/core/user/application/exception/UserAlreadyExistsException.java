package com.devfiles.core.user.application.exception;

import com.devfiles.enterprise.application.exception.AlreadyExistsException;

import java.io.Serial;

public class UserAlreadyExistsException extends AlreadyExistsException {
    @Serial
    private static final long serialVersionUID = 592238483111621583L;

    public UserAlreadyExistsException() {
        super("User already exists");
    }
}