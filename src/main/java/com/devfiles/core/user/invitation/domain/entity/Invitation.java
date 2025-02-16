package com.devfiles.core.user.invitation.domain.entity;

import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.invitation.domain.valueobject.InvitationCode;
import com.devfiles.enterprise.domain.entity.BaseDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public class Invitation extends BaseDomain {
    private final User user;
    @Setter private InvitationCode code;
    @Builder.Default
    private boolean consumed = false;

    public void consume() {
        consumed = true;
    }

    public boolean isExpired() {
        return this.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now());
    }
}
