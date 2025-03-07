package com.devfiles.enterprise.infrastructure.configuration.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.domain.valueobject.Slug;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderUnitTest {
    private static JwtTokenProvider jwtTokenProvider;
    private static User user;

    private static final String secretKey = "secret-key";
    private static final long accessTokenDurationInMs = 5000; // 5 seconds
    private static final long refreshTokenDurationInMs = 10000; // 10 seconds

    @BeforeAll
    public static void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secretKey, accessTokenDurationInMs, refreshTokenDurationInMs);

        user = mock(User.class);
        when(user.getSlug()).thenReturn(mock(Slug.class));
        when(user.getSlug().getValue()).thenReturn("user-slug");
    }

    @Test
    void shouldGenerateAccessToken() {
        var accessToken = jwtTokenProvider.generateAccessToken(user);

        assertNotNull(accessToken);

        var decodedToken = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(accessToken);

        assertEquals("user-slug", decodedToken.getSubject());
        assertEquals("devfiles.com", decodedToken.getIssuer());
        assertEquals(
                accessTokenDurationInMs,
                decodedToken.getExpiresAt().getTime() - decodedToken.getIssuedAt().getTime()
        );
    }

    @Test
    void shouldGenerateRefreshToken() {
        var refreshToken = jwtTokenProvider.generateRefreshToken(user);

        assertNotNull(refreshToken);

        var decodedToken = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(refreshToken);

        assertEquals("user-slug", decodedToken.getSubject());
        assertEquals("devfiles.com", decodedToken.getIssuer());
        assertEquals(
                refreshTokenDurationInMs,
                decodedToken.getExpiresAt().getTime() - decodedToken.getIssuedAt().getTime()
        );
    }
}