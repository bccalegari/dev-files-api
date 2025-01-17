package com.devfiles.enterprise.infrastructure.adapter.configuration.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devfiles.core.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class JwtTokenProvider {
    private final String SECRET_KEY;
    private final long ACCESS_TOKEN_DURATION_IN_MS;
    private final long REFRESH_TOKEN_DURATION_IN_MS;

    public JwtTokenProvider(
            @Value("${jwt.secret-key}") String SECRET_KEY,
            @Value("${jwt.access-token-duration-in-ms}") long ACCESS_TOKEN_DURATION_IN_MS,
            @Value("${jwt.refresh-token-duration-in-ms}") long REFRESH_TOKEN_DURATION_IN_MS
    ) {
        this.SECRET_KEY = SECRET_KEY;
        this.ACCESS_TOKEN_DURATION_IN_MS = ACCESS_TOKEN_DURATION_IN_MS;
        this.REFRESH_TOKEN_DURATION_IN_MS = REFRESH_TOKEN_DURATION_IN_MS;
    }

    public String generateAccessToken(User user) {
        return generateToken(user, Duration.ofMillis(ACCESS_TOKEN_DURATION_IN_MS));
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, Duration.ofMillis(REFRESH_TOKEN_DURATION_IN_MS));
    }

    private String generateToken(User user, Duration duration) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        return JWT.create()
                .withSubject(user.getSlug().getValue())
                .withExpiresAt(Instant.now().plus(duration))
                .withIssuedAt(Instant.now())
                .withIssuer("devfiles.com")
                .sign(algorithm);
    }
}

