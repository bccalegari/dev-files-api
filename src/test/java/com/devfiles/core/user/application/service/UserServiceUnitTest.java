package com.devfiles.core.user.application.service;

import com.devfiles.core.user.abstraction.UserRepositoryGateway;
import com.devfiles.core.user.application.exception.UserNotFoundException;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.abstraction.CacheGateway;
import com.devfiles.enterprise.domain.constant.CacheKeys;
import com.devfiles.enterprise.domain.valueobject.Slug;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock private CacheGateway cacheGateway;
    @Mock private ObjectMapper objectMapper;
    @Mock private UserRepositoryGateway userRepositoryGateway;
    @InjectMocks private UserService userService;

    @Test
    void shouldFindBySlugByCacheWhenUserIsCached() {
        var userSlug = "user-slug";
        var expectedUser = User.builder().slug(Slug.of(userSlug)).build();
        var expectedUserFromCache = mock(Object.class);

        when(cacheGateway.get(CacheKeys.USER.getKey().formatted(userSlug))).thenReturn(expectedUserFromCache);
        when(objectMapper.convertValue(expectedUserFromCache, User.class)).thenReturn(expectedUser);

        var obtainedUser = userService.findBySlug(userSlug);

        verify(cacheGateway).get(CacheKeys.USER.getKey().formatted(userSlug));
        verify(objectMapper).convertValue(expectedUserFromCache, User.class);
        verifyNoInteractions(userRepositoryGateway);

        assertEquals(expectedUser.getSlug(), obtainedUser.getSlug());
    }

    @Test
    void shouldFindBySlugByDatabaseWhenUserIsNotCached() {
        var userSlug = "user-slug";
        Optional<User> expectedUser = Optional.of(User.builder().slug(Slug.of(userSlug)).build());

        when(cacheGateway.get(CacheKeys.USER.getKey().formatted(userSlug))).thenReturn(null);
        when(userRepositoryGateway.findBySlug(userSlug)).thenReturn(expectedUser);

        var obtainedUser = userService.findBySlug(userSlug);

        verify(cacheGateway).get(CacheKeys.USER.getKey().formatted(userSlug));
        verify(userRepositoryGateway).findBySlug(userSlug);
        verify(cacheGateway).put(CacheKeys.USER.getKey().formatted(userSlug), obtainedUser, Duration.ofDays(1));

        verifyNoInteractions(objectMapper);

        assertEquals(expectedUser.get().getSlug(), obtainedUser.getSlug());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIsNotFoundBySlug() {
        var userSlug = "user-slug";

        when(cacheGateway.get(CacheKeys.USER.getKey().formatted(userSlug))).thenReturn(null);
        when(userRepositoryGateway.findBySlug(userSlug)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findBySlug(userSlug));
    }

    @Test
    void shouldDeleteUserFromCacheWhenUserIsDeleted() {
        var userSlug = "user-slug";
        var user = User.builder().slug(Slug.of(userSlug)).deletedAt(LocalDateTime.now()).build();

        when(userRepositoryGateway.save(user)).thenReturn(user);

        userService.save(user);

        verify(cacheGateway).delete(CacheKeys.USER.getKey().formatted(userSlug));
    }

    @Test
    void shouldNotDeleteUserFromCacheWhenUserIsNotDeleted() {
        var userSlug = "user-slug";
        var user = User.builder().slug(Slug.of(userSlug)).build();

        when(userRepositoryGateway.save(user)).thenReturn(user);

        userService.save(user);

        verify(cacheGateway).put(CacheKeys.USER.getKey().formatted(userSlug), user, Duration.ofDays(1));
    }
}
