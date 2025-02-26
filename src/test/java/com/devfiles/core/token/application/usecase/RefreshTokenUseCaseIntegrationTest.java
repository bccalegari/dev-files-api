package com.devfiles.core.token.application.usecase;

import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.domain.valueobject.Slug;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenDecoder;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenProvider;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RefreshTokenUseCaseIntegrationTest {
    private static JwtTokenDecoder jwtTokenDecoder;
    private static JwtTokenValidator jwtTokenValidator;
    private static JwtTokenProvider jwtTokenProvider;
    private static UserService userService;
    private static RefreshTokenUseCase refreshTokenUseCase;

    private static final String secretKey = "secretKey";
    private static final Long accessTokenExpiration = 1000L; // 1 second
    private static final Long refreshTokenExpiration = 3000L; // 3 seconds

    @BeforeAll
    public static void setUp() {
        jwtTokenDecoder = new JwtTokenDecoder(secretKey);
        jwtTokenValidator = new JwtTokenValidator();
        jwtTokenProvider = new JwtTokenProvider(secretKey, accessTokenExpiration, refreshTokenExpiration);
        userService = mock(UserService.class);
        refreshTokenUseCase = new RefreshTokenUseCase(jwtTokenDecoder, jwtTokenValidator, jwtTokenProvider, userService);
    }

    @Test
    void shouldGenerateNewAccessTokenWhenRefreshTokenIsValid() {
        var user = User.builder().slug(Slug.of("slug")).build();
        when(userService.findBySlug("slug")).thenReturn(user);

        var refreshToken = jwtTokenProvider.generateRefreshToken(userService.findBySlug("slug"));
        var response = refreshTokenUseCase.execute(refreshToken);

        verify(userService, times(3)).findBySlug("slug");

        assertNotNull(response.getData().getAccessToken());

        var decodedNewAccessToken = jwtTokenDecoder.execute(response.getData().getAccessToken());
        assertNotNull(decodedNewAccessToken);

        assertEquals("slug", decodedNewAccessToken.getSubject());

        assertTrue(jwtTokenValidator.execute(decodedNewAccessToken));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenRefreshTokenIsInvalid() {
        var refreshToken = "invalidToken";
        assertThrows(RuntimeException.class, () -> refreshTokenUseCase.execute(refreshToken));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenRefreshTokenIsExpired() {
        var user = User.builder().slug(Slug.of("slug")).build();
        when(userService.findBySlug("slug")).thenReturn(user);

        var refreshToken = jwtTokenProvider.generateRefreshToken(userService.findBySlug("slug"));
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            fail("Test failed, thread interrupted");
        }

        assertThrows(RuntimeException.class, () -> refreshTokenUseCase.execute(refreshToken));
    }
}