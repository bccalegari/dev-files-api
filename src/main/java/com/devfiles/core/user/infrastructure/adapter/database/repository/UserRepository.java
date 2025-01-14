package com.devfiles.core.user.infrastructure.adapter.database.repository;

import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsernameOrEmailAndDeletedAtIsNull(String username, String email);
}