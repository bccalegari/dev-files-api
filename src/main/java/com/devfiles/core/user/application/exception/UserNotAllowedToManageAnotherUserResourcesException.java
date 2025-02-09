package com.devfiles.core.user.application.exception;

import com.devfiles.enterprise.application.exception.ForbiddenException;

import java.io.Serial;

public class UserNotAllowedToManageAnotherUserResourcesException extends ForbiddenException {
    @Serial
    private static final long serialVersionUID = -999018179671250901L;

    public UserNotAllowedToManageAnotherUserResourcesException() {
        super("User not allowed to manage another user resources");
    }
}