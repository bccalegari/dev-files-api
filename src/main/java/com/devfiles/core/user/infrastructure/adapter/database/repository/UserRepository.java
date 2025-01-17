package com.devfiles.core.user.infrastructure.adapter.database.repository;

import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findBySlugAndDeletedAtIsNull(String slug);
    Optional<UserEntity> findByUsernameOrEmailAndDeletedAtIsNull(String username, String email);
    boolean existsByUsernameOrEmailAndDeletedAtIsNull(String username, String email);
}