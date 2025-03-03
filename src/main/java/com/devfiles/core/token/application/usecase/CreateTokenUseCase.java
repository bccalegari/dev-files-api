package com.devfiles.core.token.application.usecase;

import com.devfiles.core.token.infrastructure.adapter.dto.CreateTokenRequestDto;
import com.devfiles.core.token.infrastructure.adapter.dto.CreateTokenResponseDto;
import com.devfiles.core.user.application.exception.UserInvalidCredentialsException;
import com.devfiles.core.user.application.exception.UserNotActiveException;
import com.devfiles.core.user.application.exception.UserNotFoundException;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenProvider;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTokenUseCase {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseDto<CreateTokenResponseDto> execute(CreateTokenRequestDto createTokenRequestDto) {
        User user;

        try {
            user = userService.findByUsernameOrEmail(createTokenRequestDto.getUsername(), createTokenRequestDto.getEmail());
        } catch (UserNotFoundException e) {
            throw new UserInvalidCredentialsException();
        }

        if (!user.isActive()) {
            throw new UserNotActiveException();
        }

        if (!passwordEncoder.matches(createTokenRequestDto.getPassword(), user.getPassword())) {
            throw new UserInvalidCredentialsException();
        }

        var accessToken = jwtTokenProvider.generateAccessToken(user);
        var refreshToken = jwtTokenProvider.generateRefreshToken(user);

        var authResponseDto = CreateTokenResponseDto.builder()
                .userSlug(user.getSlug().getValue())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseDto.success(authResponseDto, "Tokens generated successfully");
    }
}
