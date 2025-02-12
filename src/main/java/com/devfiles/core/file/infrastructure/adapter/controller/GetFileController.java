package com.devfiles.core.file.infrastructure.adapter.controller;

import com.devfiles.core.file.application.usecase.GetFileUseCase;
import com.devfiles.core.file.infrastructure.adapter.dto.GetFileResponseDto;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiGetV1;
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
public class GetFileController {
    private final GetFileUseCase getFileUseCase;

    @ApiGetV1(
            summary = "Get a file",
            description = """
                    Get a file by user slug and file slug
                    """,
            tags = {"File"}
    )
    @UserAuthorizationValidator
    public ResponseEntity<ResponseDto<GetFileResponseDto>> execute(
            @RequestAttribute(name = "logged_in_user_slug") String loggedInUserSlug,
            @PathVariable(value = "user_slug") String userSlug,
            @PathVariable(value = "file_slug") String fileSlug
    ) {
        var response = getFileUseCase.execute(userSlug, fileSlug);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
