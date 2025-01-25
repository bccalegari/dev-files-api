package com.devfiles.core.token.infrastructure.adapter.controller;

import com.devfiles.core.token.application.usecase.CreateTokenUseCase;
import com.devfiles.core.token.infrastructure.adapter.dto.CreateTokenRequestDto;
import com.devfiles.core.token.infrastructure.adapter.dto.CreateTokenResponseDto;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiPostV1;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiResponseV1;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tokens")
@Tag(name = "Token", description = "Endpoints for token management")
@RequiredArgsConstructor
public class CreateTokenController {
    private final CreateTokenUseCase createTokenUseCase;

    @ApiPostV1(
            summary = "Create a new token",
            description = "Authenticate a user with username and password and return a access and refresh token",
            tags = {"Token"}
    )
    @ApiResponseV1(
            responseCode = "404",
            description = "User not found"
    )
    public ResponseEntity<ResponseDto<CreateTokenResponseDto>> execute(
            @Valid @RequestBody CreateTokenRequestDto createTokenRequestDto
    ) {
        var response = createTokenUseCase.execute(createTokenRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
