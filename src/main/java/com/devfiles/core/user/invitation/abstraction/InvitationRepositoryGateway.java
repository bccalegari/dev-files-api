package com.devfiles.core.user.invitation.abstraction;

import com.devfiles.core.user.invitation.domain.entity.Invitation;

import java.util.List;
import java.util.Optional;

public interface InvitationRepositoryGateway {
    List<Invitation> findAll();
    Optional<Invitation> findLastInvitationByUserId(Long userId);
    Invitation save(Invitation user);
    List<Invitation> saveAll(List<Invitation> invitations);
}