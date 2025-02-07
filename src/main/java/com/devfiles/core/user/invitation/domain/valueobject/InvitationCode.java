package com.devfiles.core.user.invitation.domain.valueobject;

import java.security.SecureRandom;

public record InvitationCode(String value) {
    public InvitationCode(String value) {
        if (value == null || value.isBlank()) {
            this.value = generateCode();
            return;
        }

        this.value = value;
    }

    public InvitationCode() {
        this(null);
    }

    private String generateCode() {
        return new SecureRandom().ints(0, 10)
                .limit(6)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
