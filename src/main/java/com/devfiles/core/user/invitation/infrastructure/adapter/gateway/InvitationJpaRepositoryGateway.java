package com.devfiles.core.user.invitation.infrastructure.adapter.gateway;

import com.devfiles.core.user.invitation.abstraction.InvitationRepositoryGateway;
import com.devfiles.core.user.invitation.domain.entity.Invitation;
import com.devfiles.core.user.invitation.infrastructure.adapter.database.repository.InvitationRepository;
import com.devfiles.core.user.invitation.infrastructure.adapter.mapper.InvitationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Qualifier("invitationJpaRepositoryGateway")
public class InvitationJpaRepositoryGateway implements InvitationRepositoryGateway {
    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;

    @Override
    public Optional<Invitation> findLastInvitationByUserId(Long userId) {
        return invitationRepository.findLastInvitationByUserId(userId).map(invitationMapper::toDomain);
    }

    @Override
    public Invitation save(Invitation invitation) {
        var invitationEntity = invitationMapper.toEntity(invitation);
        var savedInvitationEntity = invitationRepository.saveAndFlush(invitationEntity);
        return invitationMapper.toDomain(savedInvitationEntity);
    }

    @Override
    public int deleteExpiredInvitations() {
        var expirationDate = LocalDateTime.now().minusMinutes(15);
        return invitationRepository.deleteExpiredInvitations(expirationDate);
    }
}
