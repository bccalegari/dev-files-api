package com.devfiles.core.user.invitation.infrastructure.adapter.controller;

import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UsersInvitationsLinksFactory {
    public ResponseDto.Link resend(String slug) {
        var resend = UsersInvitationsLinks.RESEND;
        return ResponseDto.Link.builder()
                .rel(resend.getRel())
                .href(String.format(resend.getHref(), slug))
                .title(resend.getTitle())
                .type(resend.getMethod())
                .requiresAuth(resend.isRequiresAuth())
                .templated(resend.isTemplated())
                .build();
    }
}
