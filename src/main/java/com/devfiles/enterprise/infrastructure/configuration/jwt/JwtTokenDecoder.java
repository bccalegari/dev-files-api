package com.devfiles.enterprise.infrastructure.configuration.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.devfiles.enterprise.application.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenDecoder {
    private final String SECRET_KEY;

    public JwtTokenDecoder(@Value("${jwt.secret-key}") String SECRET_KEY) {
        this.SECRET_KEY = SECRET_KEY;
    }

    public DecodedJWT execute(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
        } catch (Exception e) {
            throw new BadRequestException("Invalid token");
        }
    }
}

