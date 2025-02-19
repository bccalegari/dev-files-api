package com.devfiles.core.user.infrastructure.adapter.controller;

import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UsersLinksFactory {
    public ResponseDto.Link self(String slug) {
        var self = UsersLinks.SELF;
        return ResponseDto.Link.builder()
                .rel(self.getRel())
                .href(String.format(self.getHref(), slug))
                .title(self.getTitle())
                .type(self.getMethod())
                .requiresAuth(self.isRequiresAuth())
                .templated(self.isTemplated())
                .build();
    }

    public ResponseDto.Link user(String slug) {
        var get = UsersLinks.USER;
        return ResponseDto.Link.builder()
                .rel(get.getRel())
                .href(String.format(get.getHref(), slug))
                .title(get.getTitle())
                .type(get.getMethod())
                .requiresAuth(get.isRequiresAuth())
                .templated(get.isTemplated())
                .build();
    }

    public ResponseDto.Link activate(String slug) {
        var activate = UsersLinks.ACTIVATE;
        return ResponseDto.Link.builder()
                .rel(activate.getRel())
                .href(String.format(activate.getHref(), slug))
                .title(activate.getTitle())
                .type(activate.getMethod())
                .requiresAuth(activate.isRequiresAuth())
                .templated(activate.isTemplated())
                .build();
    }

    public ResponseDto.Link create() {
        var create = UsersLinks.CREATE;
        return ResponseDto.Link.builder()
                .rel(create.getRel())
                .href(create.getHref())
                .title(create.getTitle())
                .type(create.getMethod())
                .requiresAuth(create.isRequiresAuth())
                .templated(create.isTemplated())
                .build();
    }

    public ResponseDto.Link update(String slug) {
        var update = UsersLinks.UPDATE;
        return ResponseDto.Link.builder()
                .rel(update.getRel())
                .href(String.format(update.getHref(), slug))
                .title(update.getTitle())
                .type(update.getMethod())
                .requiresAuth(update.isRequiresAuth())
                .templated(update.isTemplated())
                .build();
    }

    public ResponseDto.Link delete(String slug) {
        var delete = UsersLinks.DELETE;
        return ResponseDto.Link.builder()
                .rel(delete.getRel())
                .href(String.format(delete.getHref(), slug))
                .title(delete.getTitle())
                .type(delete.getMethod())
                .requiresAuth(delete.isRequiresAuth())
                .templated(delete.isTemplated())
                .build();
    }
}
