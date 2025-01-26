package com.devfiles.core.user.invitation.infrastructure.adapter.mapper;

import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import com.devfiles.core.user.invitation.domain.entity.Invitation;
import com.devfiles.core.user.invitation.domain.valueobject.InvitationCode;
import com.devfiles.core.user.invitation.infrastructure.adapter.database.entity.InvitationEntity;
import com.devfiles.enterprise.domain.valueobject.Slug;
import org.springframework.stereotype.Component;

@Component
public class InvitationMapper {
    public InvitationEntity toEntity(Invitation invitation) {
        return InvitationEntity.builder()
                .id(invitation.getId())
                .slug(invitation.getSlug().getValue())
                .user(UserEntity.builder().id(invitation.getUser().getId()).build())
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
                .user(User.builder().id(invitationEntity.getUser().getId()).build())
                .code(new InvitationCode(invitationEntity.getCode()))
                .consumed(invitationEntity.isConsumed())
                .createdAt(invitationEntity.getCreatedAt())
                .updatedAt(invitationEntity.getUpdatedAt())
                .deletedAt(invitationEntity.getDeletedAt())
                .build();
    }
}
