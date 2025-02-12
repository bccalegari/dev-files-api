package com.devfiles.core.file.infrastructure.adapter.controller;

import com.devfiles.core.file.application.usecase.LocalGetFileResourceUseCase;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiGetV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "users/{user_slug}/files/{file_slug}/resources")
@Tag(name = "File", description = "Endpoints for file management")
@RequiredArgsConstructor
@Profile("local")
public class LocalGetFileResourceController {
    private final LocalGetFileResourceUseCase localGetFileResourceUseCase;

    @ApiGetV1(
            summary = "Get file resource [LOCAL]",
            description = """
                    Get file resource by user slug and file slug [LOCAL]
                    """,
            tags = {"File"}
    )
    public ResponseEntity<Resource> execute(
            @PathVariable(value = "user_slug") String slug,
            @PathVariable(value = "file_slug") String fileSlug
    ) {
        var response = localGetFileResourceUseCase.execute(slug, fileSlug);
        var headerValue = "inline; filename=\"" + response.getFileNameWithExtension() + "\"";

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(response.getMediaType())
                .header("Content-Disposition", headerValue)
                .body(response.getUrlResource());
    }
}
