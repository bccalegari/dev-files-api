package com.devfiles.core.user.invitation.domain.entity;

import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.invitation.domain.valueobject.InvitationCode;
import com.devfiles.enterprise.domain.entity.BaseDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class Invitation extends BaseDomain {
    private final User user;
    private final InvitationCode code;
    private boolean consumed;

    public void consume() {
        consumed = true;
    }
}
