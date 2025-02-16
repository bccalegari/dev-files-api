package com.devfiles.core.file.infrastructure.adapter.controller;

import com.devfiles.core.file.application.usecase.GetAllFilesUseCase;
import com.devfiles.core.file.infrastructure.adapter.dto.GetAllFilesRequestParams;
import com.devfiles.core.file.infrastructure.adapter.dto.GetAllFilesResponseDto;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiGetV1;
import com.devfiles.enterprise.infrastructure.adapter.annotation.UserAuthorizationValidator;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users/{user_slug}/files")
@Tag(name = "File", description = "Endpoints for file management")
@RequiredArgsConstructor
public class GetAllFilesController {
    private final GetAllFilesUseCase getAllFilesUseCase;

    @ApiGetV1(
            summary = "Get all files",
            description = """
                    Get all files of a user by user slug
                    """,
            tags = {"File"}
    )
    @UserAuthorizationValidator
    public ResponseEntity<ResponseDto<List<GetAllFilesResponseDto>>> execute(
            @RequestAttribute(name = "logged_in_user_slug") String loggedInUserSlug,
            @PathVariable(value = "user_slug") String userSlug,
            @Valid @ModelAttribute GetAllFilesRequestParams requestParams

    ) {
        var response = getAllFilesUseCase.execute(userSlug, requestParams);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
