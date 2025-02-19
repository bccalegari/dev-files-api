package com.devfiles.core.file.infrastructure.adapter.controller;

import com.devfiles.core.file.application.usecase.CreateFileUseCase;
import com.devfiles.core.file.infrastructure.adapter.dto.CreateFileResponseDto;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiPostV1;
import com.devfiles.enterprise.infrastructure.adapter.annotation.UserAuthorizationValidator;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "users/{user_slug}/files")
@Tag(name = "File", description = "Endpoints for file management")
@RequiredArgsConstructor
public class CreateFileController {
    private final CreateFileUseCase createFileUseCase;
    private final FilesLinksFactory filesLinksFactory;

    @ApiPostV1(
            summary = "Create a new file",
            description = """
                    Create a new file, only accepts files with size equal or less
                    than 50MB and with the following mime types: image/jpeg, image/png, application/pdf
                    """,
            tags = {"File"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @UserAuthorizationValidator
    public ResponseEntity<ResponseDto<CreateFileResponseDto>> execute(
            @RequestAttribute(name = "logged_in_user_slug") String loggedInUserSlug,
            @PathVariable(value = "user_slug") String userSlug,
            @RequestPart(value = "file") @NotNull(message = "File is required") MultipartFile file
    ) {
        var response = createFileUseCase.execute(userSlug, file);
        response.createLinks(
                List.of(
                        filesLinksFactory.self(loggedInUserSlug, response.getData().getSlug()),
                        filesLinksFactory.files(loggedInUserSlug),
                        filesLinksFactory.delete(loggedInUserSlug, response.getData().getSlug())
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
