package com.devfiles.core.user.invitation.infrastructure.adapter.mapper;

import com.devfiles.core.user.infrastructure.adapter.mapper.UserMapper;
import com.devfiles.core.user.invitation.domain.entity.Invitation;
import com.devfiles.core.user.invitation.domain.valueobject.InvitationCode;
import com.devfiles.core.user.invitation.infrastructure.adapter.database.entity.InvitationEntity;
import com.devfiles.enterprise.domain.valueobject.Slug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvitationMapper {
    private final UserMapper userMapper;

    public InvitationEntity toEntity(Invitation invitation) {
        return InvitationEntity.builder()
                .id(invitation.getId())
                .slug(invitation.getSlug().getValue())
                .user(userMapper.toEntity(invitation.getUser()))
                .code(invitation.getCode().value())
                .consumed(invitation.isConsumed())
                .createdAt(invitation.getCreatedAt())
                .updatedAt(invitation.getUpdatedAt())
                .deletedAt(invitation.getDeletedAt())
                .build();
    }

    public Invitation toDomain(InvitationEntity invitationEntity) {
        return Invitation.builder()
                .id(invitationEntity.getId())
                .slug(Slug.of(invitationEntity.getSlug()))
                .user(userMapper.toDomain(invitationEntity.getUser()))
                .code(new InvitationCode(invitationEntity.getCode()))
                .consumed(invitationEntity.isConsumed())
                .createdAt(invitationEntity.getCreatedAt())
                .updatedAt(invitationEntity.getUpdatedAt())
                .deletedAt(invitationEntity.getDeletedAt())
                .build();
    }
}
