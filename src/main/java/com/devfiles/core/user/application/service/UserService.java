package com.devfiles.core.user.application.service;

import com.devfiles.core.user.abstraction.UserRepositoryGateway;
import com.devfiles.core.user.application.exception.UserNotFoundException;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.abstraction.CacheGateway;
import com.devfiles.enterprise.domain.constant.CacheKeys;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UserService {
    private final CacheGateway cacheGateway;
    private final ObjectMapper objectMapper;
    private final UserRepositoryGateway userRepositoryGateway;

    public UserService(
            @Qualifier("redisCacheGateway") CacheGateway cacheGateway,
            ObjectMapper objectMapper,
            @Qualifier("userJpaRepositoryGateway") UserRepositoryGateway userRepositoryGateway
    ) {
        this.cacheGateway = cacheGateway;
        this.objectMapper = objectMapper;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    public User findBySlug(String slug) {
        var userFromCache = cacheGateway.get(CacheKeys.USER.getKey().formatted(slug));

        if (userFromCache != null) {
            return objectMapper.convertValue(userFromCache, User.class);
        }

        var userOp = userRepositoryGateway.findBySlug(slug);

        if (userOp.isEmpty()) {
            throw new UserNotFoundException();
        }

        var user = userOp.get();

        cacheGateway.put(CacheKeys.USER.getKey().formatted(slug), user, Duration.ofDays(1));

        return user;
    }

    public User findByUsernameOrEmail(String username, String email) {
        var userOp = userRepositoryGateway.findByUsernameOrEmail(username, email);

        if (userOp.isEmpty()) {
            throw new UserNotFoundException();
        }

        return userOp.get();
    }

    public boolean exists(User user) {
        return userRepositoryGateway.exists(user);
    }

    public User save(User user) {
        return userRepositoryGateway.save(user);
    }

    public User activateUser(User user) {
        user.activate();
        return save(user);
    }

    public boolean isUserAllowedToManageAnotherUserResources(String loggedInUserSlug, String userSlug) {
        return loggedInUserSlug.equals(userSlug);
    }
}
