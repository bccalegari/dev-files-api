package com.devfiles.core.user.invitation.infrastructure.adapter.database.entity;

import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import com.devfiles.enterprise.infrastructure.adapter.database.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "\"user\"", name = "invitations")
public class InvitationEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "consumed", nullable = false)
    private boolean consumed;
}