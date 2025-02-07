package com.devfiles.core.user.invitation.infrastructure.adapter.database.repository;

import com.devfiles.core.user.invitation.infrastructure.adapter.database.entity.InvitationEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, Long> {
    @Query(value = """
        SELECT i
        FROM InvitationEntity i
        WHERE i.deletedAt IS NULL
        AND i.consumed = FALSE
        ORDER BY i.id DESC
    """)
    List<InvitationEntity> findAll();

    @Query(value = """
        SELECT i
        FROM InvitationEntity i
        WHERE i.user.id = :userId
        AND i.deletedAt IS NULL
        AND i.consumed = FALSE
        ORDER BY i.id DESC
    """)
    Optional<InvitationEntity> findLastInvitationByUserId(@Param("userId") Long userId);
}