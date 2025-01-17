package com.devfiles.core.token.application.usecase;

import com.devfiles.core.token.infrastructure.adapter.dto.CreateTokenRequestDto;
import com.devfiles.core.token.infrastructure.adapter.dto.CreateTokenResponseDto;
import com.devfiles.core.user.abstraction.UserRepositoryGateway;
import com.devfiles.core.user.application.exception.UserInvalidCredentialsException;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenProvider;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTokenUseCase {
    private final UserRepositoryGateway userRepositoryGateway;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseDto<CreateTokenResponseDto> execute(CreateTokenRequestDto createTokenRequestDto) {
        var user = userRepositoryGateway.findByUsernameOrEmail(createTokenRequestDto.getUsername(), createTokenRequestDto.getEmail())
                .orElseThrow(UserInvalidCredentialsException::new);

        if (!bCryptPasswordEncoder.matches(createTokenRequestDto.getPassword(), user.getPassword())) {
            throw new UserInvalidCredentialsException();
        }

        var accessToken = jwtTokenProvider.generateAccessToken(user);
        var refreshToken = jwtTokenProvider.generateRefreshToken(user);

        var authResponseDto = new CreateTokenResponseDto(accessToken, refreshToken);

        return ResponseDto.success(authResponseDto, "Tokens generated successfully");
    }
}
