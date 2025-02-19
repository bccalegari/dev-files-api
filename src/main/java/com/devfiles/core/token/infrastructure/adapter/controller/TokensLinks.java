package com.devfiles.core.token.infrastructure.adapter.controller;

import com.devfiles.enterprise.infrastructure.adapter.controller.AbstractLinks;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokensLinks implements AbstractLinks {
    CREATE("create", "/tokens", "Create a new token for user authentication", "POST",
            false, false),
    REFRESH("refresh", "/tokens/refresh", "Refresh the access token using a valid refresh token",
            "POST", true, false);

    private final String rel;
    private final String href;
    private final String title;
    private final String method;
    private final boolean requiresAuth;
    private final boolean templated;
}
