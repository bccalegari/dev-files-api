package com.devfiles.core.token.infrastructure.adapter.controller;

import com.devfiles.core.token.application.usecase.RefreshTokenUseCase;
import com.devfiles.core.token.infrastructure.adapter.dto.RefreshTokenRequestDto;
import com.devfiles.core.token.infrastructure.adapter.dto.RefreshTokenResponseDto;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiPatchV1;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/tokens")
@Tag(name = "Token", description = "Endpoints for authentication")
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final TokensLinksFactory tokensLinksFactory;

    @ApiPatchV1(
            responseCode = "200",
            responseDescription = "OK",
            summary = "Refresh token",
            description = "Generate a new access token from a refresh token",
            tags = {"Token"}
    )
    public ResponseEntity<ResponseDto<RefreshTokenResponseDto>> execute(
            @Valid @RequestBody RefreshTokenRequestDto refreshToken
    ) {
        var response = refreshTokenUseCase.execute(refreshToken.getToken());
        response.createLinks(
                List.of(
                        tokensLinksFactory.create()
                )
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
