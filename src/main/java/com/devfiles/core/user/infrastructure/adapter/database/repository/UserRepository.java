package com.devfiles.core.user.infrastructure.adapter.database.repository;

import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findBySlugAndDeletedAtIsNull(String slug);

    @Query(value = """
        SELECT u
        FROM UserEntity u
        WHERE (u.username = :username OR u.email = :email)
        AND u.deletedAt IS NULL
    """)
    Optional<UserEntity> findByUsernameOrEmailAndDeletedAtIsNull(String username, String email);

    boolean existsByIdAndDeletedAtIsNull(Long id);

    @Query(value = """
        SELECT CASE
            WHEN COUNT(u) > 0 THEN TRUE
            ELSE FALSE
        END
        FROM UserEntity u
        WHERE (u.username = :username OR u.email = :email)
        AND u.deletedAt IS NULL
    """)
    boolean existsByUsernameOrEmailAndDeletedAtIsNull(String username, String email);

    boolean existsByUsernameAndDeletedAtIsNull(String username);
    boolean existsByEmailAndDeletedAtIsNull(String email);
}