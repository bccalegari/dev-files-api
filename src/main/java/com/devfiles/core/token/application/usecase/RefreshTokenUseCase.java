package com.devfiles.core.token.application.usecase;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.devfiles.core.token.infrastructure.adapter.dto.RefreshTokenResponseDto;
import com.devfiles.core.user.abstraction.UserRepositoryGateway;
import com.devfiles.core.user.application.exception.UserNotFoundException;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenDecoder;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenProvider;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenValidator;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {
    private final JwtTokenDecoder jwtTokenDecoder;
    private final JwtTokenValidator jwtTokenValidator;
    private final UserRepositoryGateway userRepositoryGateway;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseDto<RefreshTokenResponseDto> execute(String refreshToken) {
        DecodedJWT decodedJWT = jwtTokenDecoder.execute(refreshToken);
        String slug = decodedJWT.getSubject();

        if (jwtTokenValidator.execute(decodedJWT)) {
            throw new RuntimeException("Refresh token is expired");
        }

        var user = userRepositoryGateway.findBySlug(slug)
                .orElseThrow(UserNotFoundException::new);

        var token = jwtTokenProvider.generateAccessToken(user);
        var refreshTokenResponseDto = new RefreshTokenResponseDto(token);

        return ResponseDto.success(refreshTokenResponseDto, "Token refreshed successfully");
    }
}
