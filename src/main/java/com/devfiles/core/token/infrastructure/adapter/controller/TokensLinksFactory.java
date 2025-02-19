package com.devfiles.core.token.infrastructure.adapter.controller;

import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TokensLinksFactory {
    public ResponseDto.Link create() {
        var create = TokensLinks.CREATE;
        return ResponseDto.Link.builder()
                .rel(create.getRel())
                .href(create.getHref())
                .title(create.getTitle())
                .type(create.getMethod())
                .requiresAuth(create.isRequiresAuth())
                .templated(create.isTemplated())
                .build();
    }

    public ResponseDto.Link refresh() {
        var refresh = TokensLinks.REFRESH;
        return ResponseDto.Link.builder()
                .rel(refresh.getRel())
                .href(refresh.getHref())
                .title(refresh.getTitle())
                .type(refresh.getMethod())
                .requiresAuth(refresh.isRequiresAuth())
                .templated(refresh.isTemplated())
                .build();
    }
}
