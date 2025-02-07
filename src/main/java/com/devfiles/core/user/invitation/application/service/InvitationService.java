package com.devfiles.core.user.invitation.application.service;

import com.devfiles.core.user.invitation.abstraction.InvitationRepositoryGateway;
import com.devfiles.core.user.invitation.application.exception.InvitationNotFoundException;
import com.devfiles.core.user.invitation.domain.entity.Invitation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {
    private final InvitationRepositoryGateway invitationRepositoryGateway;

    public InvitationService(
            @Qualifier("invitationJpaRepositoryGateway") InvitationRepositoryGateway invitationRepositoryGateway
    ) {
        this.invitationRepositoryGateway = invitationRepositoryGateway;
    }

    public Invitation findLastInvitationByUserId(Long userId) {
        var invitationOp = invitationRepositoryGateway.findLastInvitationByUserId(userId);

        if (invitationOp.isEmpty()) {
            throw new InvitationNotFoundException();
        }

        return invitationOp.get();
    }

    public Invitation save(Invitation invitation) {
        return invitationRepositoryGateway.save(invitation);
    }

    public Invitation consume(Invitation invitation) {
        invitation.consume();
        return invitationRepositoryGateway.save(invitation);
    }

    public int deleteExpiredInvitations() {
        var invitations = invitationRepositoryGateway.findAll();

        invitations.stream()
                .filter(Invitation::isExpired)
                .forEach(Invitation::delete);

        invitationRepositoryGateway.saveAll(invitations);

        return invitations.size();
    }
}
