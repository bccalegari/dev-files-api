package com.devfiles.core.user.infrastructure.adapter.controller;

import com.devfiles.core.file.infrastructure.adapter.controller.FilesLinksFactory;
import com.devfiles.core.user.application.usecase.GetUserUseCase;
import com.devfiles.core.user.infrastructure.adapter.dto.GetUserResponseDto;
import com.devfiles.enterprise.infrastructure.annotation.ApiGetV1;
import com.devfiles.enterprise.infrastructure.annotation.UserAuthorizationValidator;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Tag(name = "User", description = "Endpoints for user management")
@RequiredArgsConstructor
public class GetUserController {
    private final GetUserUseCase getUserUseCase;
    private final UsersLinksFactory usersLinksFactory;
    private final FilesLinksFactory filesLinksFactory;

    @ApiGetV1(
            path = "/{user_slug}",
            summary = "Get a user",
            description = "Get your user information by slug",
            tags = {"User"}
    )
    @UserAuthorizationValidator
    public ResponseEntity<ResponseDto<GetUserResponseDto>> execute(
            @RequestAttribute(name = "logged_in_user_slug") String loggedInUserSlug,
            @PathVariable(value = "user_slug") String userSlug
    ) {
        var response = getUserUseCase.execute(userSlug);
        response.createLinks(
                List.of(
                        usersLinksFactory.self(response.getData().getSlug()),
                        filesLinksFactory.files(response.getData().getSlug()),
                        usersLinksFactory.update(response.getData().getSlug()),
                        usersLinksFactory.delete(response.getData().getSlug())
                )
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
