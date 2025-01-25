package com.devfiles.core.user.application.usecase;

import com.devfiles.core.user.abstraction.UserRepositoryGateway;
import com.devfiles.core.user.application.exception.UserNotAllowedToGetAnotherUserException;
import com.devfiles.core.user.application.exception.UserNotFoundException;
import com.devfiles.core.user.infrastructure.adapter.dto.GetUserResponseDto;
import com.devfiles.enterprise.domain.constant.CacheKeys;
import com.devfiles.enterprise.infrastructure.adapter.abstraction.CacheGateway;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class GetUserUseCase {
    private final CacheGateway cacheGateway;
    private final ObjectMapper objectMapper;
    private final UserRepositoryGateway userRepositoryGateway;

    public GetUserUseCase(
            @Qualifier("redisCacheGateway") CacheGateway cacheGateway,
            ObjectMapper objectMapper,
            @Qualifier("userJpaRepositoryGateway") UserRepositoryGateway userRepositoryGateway
    ) {
        this.cacheGateway = cacheGateway;
        this.objectMapper = objectMapper;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    public ResponseDto<GetUserResponseDto> execute(String loggedInUserSlug, String slug) {
        if (!isUserAllowedToGetUser(loggedInUserSlug, slug)) {
            throw new UserNotAllowedToGetAnotherUserException();
        }

        var userFromCache = cacheGateway.get(CacheKeys.USER.getKey().formatted(slug));

        if (userFromCache != null) {
            var userFromCacheDto = objectMapper.convertValue(userFromCache, GetUserResponseDto.class);
            return ResponseDto.success(userFromCacheDto, "User found successfully");
        }

        var userOp = userRepositoryGateway.findBySlug(slug);

        if (userOp.isEmpty()) {
            throw new UserNotFoundException();
        }

        var user = userOp.get();

        var getUserResponseDto = GetUserResponseDto.builder()
            .slug(user.getSlug().getValue())
            .email(user.getEmail())
            .username(user.getUsername())
            .active(user.isActive())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();

        cacheGateway.put(CacheKeys.USER.getKey().formatted(slug), getUserResponseDto, Duration.ofDays(1));

        return ResponseDto.success(getUserResponseDto, "User found successfully");
    }

    private boolean isUserAllowedToGetUser(String loggedInUserSlug, String userSlug) {
        return loggedInUserSlug.equals(userSlug);
    }
}
