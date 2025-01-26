package com.devfiles.core.user.invitation.application.exception;

import com.devfiles.enterprise.application.exception.NotFoundException;

import java.io.Serial;

public class InvitationNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = -3400696988213018307L;

    public InvitationNotFoundException() {
        super("Invitation not found");
    }
}