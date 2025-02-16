package com.devfiles.core.file.infrastructure.adapter.controller;

import com.devfiles.core.file.application.usecase.DeleteFileUseCase;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiDeleteV1;
import com.devfiles.enterprise.infrastructure.adapter.annotation.UserAuthorizationValidator;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "users/{user_slug}/files/{file_slug}")
@Tag(name = "File", description = "Endpoints for file management")
@RequiredArgsConstructor
public class DeleteFileController {
    private final DeleteFileUseCase deleteFileUseCase;

    @ApiDeleteV1(
            summary = "Delete a file",
            description = """
                    Delete a file by user slug and file slug
                    """,
            tags = {"File"}
    )
    @UserAuthorizationValidator
    public ResponseEntity<ResponseDto<ResponseDto.Empty>> execute(
            @RequestAttribute(name = "logged_in_user_slug") String loggedInUserSlug,
            @PathVariable(value = "user_slug") String userSlug,
            @PathVariable(value = "file_slug") String fileSlug
    ) {
        var response = deleteFileUseCase.execute(userSlug, fileSlug);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
