package com.devfiles.core.user.infrastructure.adapter.controller;

import com.devfiles.core.user.application.usecase.CreateUserUseCase;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserRequestDto;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserResponseDto;
import com.devfiles.core.user.invitation.infrastructure.adapter.controller.UsersInvitationsLinksFactory;
import com.devfiles.enterprise.infrastructure.annotation.ApiPostV1;
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
@RequestMapping(path = "/users")
@Tag(name = "User", description = "Endpoints for user management")
@RequiredArgsConstructor
public class CreateUserController {
    private final CreateUserUseCase createUserUseCase;
    private final UsersLinksFactory usersLinksFactory;
    private final UsersInvitationsLinksFactory usersInvitationsLinksFactory;

    @ApiPostV1(
            summary = "Create a new user",
            description = "Create a new user",
            tags = {"User"}
    )
    public ResponseEntity<ResponseDto<CreateUserResponseDto>> execute(
            @Valid @RequestBody CreateUserRequestDto createUserRequestDto
    ) {
        var response = createUserUseCase.execute(createUserRequestDto);
        response.createLinks(
                List.of(
                        usersLinksFactory.activate(response.getData().getSlug()),
                        usersInvitationsLinksFactory.resend(response.getData().getSlug())
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
