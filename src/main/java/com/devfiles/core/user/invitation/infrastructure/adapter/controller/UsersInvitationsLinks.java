package com.devfiles.core.user.invitation.infrastructure.adapter.controller;

import com.devfiles.enterprise.infrastructure.adapter.controller.AbstractLinks;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UsersInvitationsLinks implements AbstractLinks {
    RESEND("resend", "/users/invitations/%s/resend", "Resend an invitation", "PATCH",
            false, false);

    private final String rel;
    private final String href;
    private final String title;
    private final String method;
    private final boolean requiresAuth;
    private final boolean templated;
}