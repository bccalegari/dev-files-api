package com.devfiles.enterprise.infrastructure.adapter.configuration.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtTokenValidator {
    public boolean execute(DecodedJWT decodedJWT) {
        var isExpired = decodedJWT.getExpiresAtAsInstant().isBefore(Instant.now());
        var isInvalidIssuer = !decodedJWT.getIssuer().equals("devfiles.com");

        return isExpired || isInvalidIssuer;
    }
}

