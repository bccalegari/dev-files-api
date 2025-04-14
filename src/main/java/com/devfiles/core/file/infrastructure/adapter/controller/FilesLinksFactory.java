package com.devfiles.core.file.infrastructure.adapter.controller;

import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import org.springframework.stereotype.Component;

@Component
public class FilesLinksFactory {
    public ResponseDto.Link self(String userSlug, String fileSlug) {
        return ResponseDto.Link.builder()
                .rel(FilesLinks.SELF.getRel())
                .href(String.format(FilesLinks.SELF.getHref(), userSlug, fileSlug))
                .title(FilesLinks.SELF.getTitle())
                .requiresAuth(FilesLinks.SELF.isRequiresAuth())
                .templated(FilesLinks.SELF.isTemplated())
                .build();
    }

    public ResponseDto.Link file(String userSlug) {
        return ResponseDto.Link.builder()
                .rel(FilesLinks.FILE.getRel())
                .href(String.format(FilesLinks.FILE.getHref(), userSlug))
                .title(FilesLinks.FILE.getTitle())
                .requiresAuth(FilesLinks.FILE.isRequiresAuth())
                .templated(FilesLinks.FILE.isTemplated())
                .build();
    }

    public ResponseDto.Link files(String userSlug) {
        return ResponseDto.Link.builder()
                .rel(FilesLinks.FILES.getRel())
                .href(String.format(FilesLinks.FILES.getHref(), userSlug))
                .title(FilesLinks.FILES.getTitle())
                .requiresAuth(FilesLinks.FILES.isRequiresAuth())
                .templated(FilesLinks.FILES.isTemplated())
                .build();
    }

    public ResponseDto.Link filesPrevious(
            String userSlug, int page, int limit, String search, String sort, String sortBy
    ) {
        return ResponseDto.Link.builder()
                .rel(FilesLinks.FILES_PREVIOUS.getRel())
                .href(String.format(FilesLinks.FILES_PREVIOUS.getHref(), userSlug, page, limit, search, sort, sortBy))
                .title(FilesLinks.FILES_PREVIOUS.getTitle())
                .requiresAuth(FilesLinks.FILES_PREVIOUS.isRequiresAuth())
                .templated(FilesLinks.FILES_PREVIOUS.isTemplated())
                .build();
    }

    public ResponseDto.Link filesNext(
            String userSlug, int page, int limit, String search, String sort, String sortBy
    ) {
        return ResponseDto.Link.builder()
                .rel(FilesLinks.FILES_NEXT.getRel())
                .href(String.format(FilesLinks.FILES_NEXT.getHref(), userSlug, page, limit, search, sort, sortBy))
                .title(FilesLinks.FILES_NEXT.getTitle())
                .requiresAuth(FilesLinks.FILES_NEXT.isRequiresAuth())
                .templated(FilesLinks.FILES_NEXT.isTemplated())
                .build();
    }

    public ResponseDto.Link create(String userSlug) {
        return ResponseDto.Link.builder()
                .rel(FilesLinks.CREATE.getRel())
                .href(String.format(FilesLinks.CREATE.getHref(), userSlug))
                .title(FilesLinks.CREATE.getTitle())
                .requiresAuth(FilesLinks.CREATE.isRequiresAuth())
                .templated(FilesLinks.CREATE.isTemplated())
                .build();
    }

    public ResponseDto.Link delete(String userSlug, String fileSlug) {
        return ResponseDto.Link.builder()
                .rel(FilesLinks.DELETE.getRel())
                .href(String.format(FilesLinks.DELETE.getHref(), userSlug, fileSlug))
                .title(FilesLinks.DELETE.getTitle())
                .requiresAuth(FilesLinks.DELETE.isRequiresAuth())
                .templated(FilesLinks.DELETE.isTemplated())
                .build();
    }

    public ResponseDto.Link query(String userSlug, String fileSlug) {
        return ResponseDto.Link.builder()
                .rel(FilesLinks.QUERY.getRel())
                .href(String.format(FilesLinks.QUERY.getHref(), userSlug, fileSlug))
                .title(FilesLinks.QUERY.getTitle())
                .requiresAuth(FilesLinks.QUERY.isRequiresAuth())
                .templated(FilesLinks.QUERY.isTemplated())
                .build();
    }
}
