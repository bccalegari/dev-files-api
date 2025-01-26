package com.devfiles.core.user.invitation.abstraction;

import com.devfiles.core.user.invitation.domain.entity.Invitation;

import java.util.Optional;

public interface InvitationRepositoryGateway {
    Optional<Invitation> findLastInvitationByUserId(Long userId);
    Invitation save(Invitation user);
    int deleteExpiredInvitations();
}