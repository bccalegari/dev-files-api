package com.devfiles.core.user.infrastructure.adapter.controller;

import com.devfiles.core.user.application.usecase.UpdateUserUseCase;
import com.devfiles.core.user.infrastructure.adapter.dto.UpdateUserRequestDto;
import com.devfiles.core.user.infrastructure.adapter.dto.UpdateUserResponseDto;
import com.devfiles.enterprise.infrastructure.annotation.ApiPatchV1;
import com.devfiles.enterprise.infrastructure.annotation.UserAuthorizationValidator;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Tag(name = "User", description = "Endpoints for user management")
@RequiredArgsConstructor
public class UpdateUserController {
    private final UpdateUserUseCase updateUserUseCase;
    private final UsersLinksFactory usersLinksFactory;

    @ApiPatchV1(
            path = "/{user_slug}",
            summary = "Update user information",
            description = "Update user information by user slug",
            tags = {"User"}
    )
    @UserAuthorizationValidator
    public ResponseEntity<ResponseDto<UpdateUserResponseDto>> execute(
            @RequestAttribute(name = "logged_in_user_slug") String loggedInUserSlug,
            @PathVariable(value = "user_slug") String userSlug,
            @Valid @RequestBody UpdateUserRequestDto updateUserRequestDto
    ) {
        var response = updateUserUseCase.execute(userSlug, updateUserRequestDto);
        response.createLinks(
                List.of(
                        usersLinksFactory.self(response.getData().getSlug()),
                        usersLinksFactory.delete(response.getData().getSlug())
                )
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
