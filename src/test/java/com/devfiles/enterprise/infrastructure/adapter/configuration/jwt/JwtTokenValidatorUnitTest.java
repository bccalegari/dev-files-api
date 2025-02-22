package com.devfiles.enterprise.infrastructure.adapter.configuration.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtTokenValidatorUnitTest {
    private final JwtTokenValidator jwtTokenValidator = new JwtTokenValidator();

    @Test
    void shouldReturnTrueWhenTokenIsValid() {
        var decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getExpiresAtAsInstant()).thenReturn(Instant.now().plus(Duration.ofDays(1)));
        when(decodedJWT.getIssuer()).thenReturn("devfiles.com");

        var result = jwtTokenValidator.execute(decodedJWT);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsExpired() {
        var decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getExpiresAtAsInstant()).thenReturn(Instant.now().minus(Duration.ofDays(1)));
        when(decodedJWT.getIssuer()).thenReturn("devfiles.com");

        var result = jwtTokenValidator.execute(decodedJWT);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenHasInvalidIssuer() {
        var decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getExpiresAtAsInstant()).thenReturn(Instant.now().plus(Duration.ofDays(1)));
        when(decodedJWT.getIssuer()).thenReturn("invalid-issuer.com");

        var result = jwtTokenValidator.execute(decodedJWT);

        assertFalse(result);
    }
}