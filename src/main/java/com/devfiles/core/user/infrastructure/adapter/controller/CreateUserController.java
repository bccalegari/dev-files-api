package com.devfiles.core.user.infrastructure.adapter.controller;

import com.devfiles.core.user.application.usecase.CreateUserUseCase;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserRequestDto;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserResponseDto;
import com.devfiles.enterprise.abstraction.UseCase;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiPostV1;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Endpoints for user management")
public class CreateUserController {
    private final UseCase<CreateUserRequestDto, CreateUserResponseDto> useCase;

    public CreateUserController(
            @Qualifier("createUserUseCase") CreateUserUseCase useCase
    ) {
        this.useCase = useCase;
    }

    @ApiPostV1(
            path = "/",
            summary = "Create a new user",
            description = "Create a new user",
            tags = {"User"}
    )
    public ResponseEntity<ResponseDto<CreateUserResponseDto>> execute(
            @Valid @RequestBody CreateUserRequestDto createUserRequestDto
    ) {
        var response = useCase.execute(createUserRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
