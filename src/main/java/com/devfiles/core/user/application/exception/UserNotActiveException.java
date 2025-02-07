package com.devfiles.core.user.application.exception;

import com.devfiles.enterprise.application.exception.UnauthorizedException;

import java.io.Serial;

public class UserNotActiveException extends UnauthorizedException {
    @Serial
    private static final long serialVersionUID = -1540325535897256845L;

    public UserNotActiveException() {
        super("User not active, please activate your account");
    }
}