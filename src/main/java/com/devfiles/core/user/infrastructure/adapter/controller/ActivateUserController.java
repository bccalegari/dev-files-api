package com.devfiles.core.user.infrastructure.adapter.controller;

import com.devfiles.core.token.infrastructure.adapter.controller.TokensLinksFactory;
import com.devfiles.core.user.application.usecase.ActivateUserUseCase;
import com.devfiles.core.user.infrastructure.adapter.dto.ActivateUserResponseDto;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiPatchV1;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Tag(name = "User", description = "Endpoints for user management")
@RequiredArgsConstructor
public class ActivateUserController {
    private final ActivateUserUseCase activateUserUseCase;
    private final UsersLinksFactory usersLinksFactory;
    private final TokensLinksFactory tokensLinksFactory;

    @ApiPatchV1(
            path = "/{user_slug}/active",
            summary = "Active user",
            description = "Active an user by slug and activation code",
            tags = {"User"}
    )
    public ResponseEntity<ResponseDto<ActivateUserResponseDto>> execute(
            @PathVariable(value = "user_slug") String userSlug,
            @RequestParam(value = "code") String code
    ) {
        var response = activateUserUseCase.execute(userSlug, code);
        response.createLinks(
                List.of(
                        tokensLinksFactory.create()
                )
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
