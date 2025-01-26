package com.devfiles.core.user.invitation.infrastructure.adapter.database.repository;

import com.devfiles.core.user.invitation.infrastructure.adapter.database.entity.InvitationEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, Long> {
    @Query(value = """
        SELECT i
        FROM InvitationEntity i
        WHERE i.user.id = :userId
        AND i.deletedAt IS NULL
        AND i.consumed = FALSE
        ORDER BY i.createdAt DESC
    """)
    Optional<InvitationEntity> findLastInvitationByUserId(@Param("userId") Long userId);

    @Query(value = """
        UPDATE InvitationEntity i
        SET i.deletedAt = CURRENT_TIMESTAMP
        WHERE i.createdAt < :expirationDate
    """)
    @Modifying
    int deleteExpiredInvitations(@Param("expirationDate") LocalDateTime expirationDate);
}