package com.devfiles.core.user.abstraction;

import com.devfiles.core.user.domain.User;

import java.util.Optional;

public interface UserRepositoryGateway {
    Optional<User> findBySlug(String slug);
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean exists(User user);
    User save(User user);
}