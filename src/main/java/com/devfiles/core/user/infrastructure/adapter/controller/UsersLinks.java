package com.devfiles.core.user.infrastructure.adapter.controller;

import com.devfiles.enterprise.infrastructure.adapter.controller.AbstractLinks;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UsersLinks implements AbstractLinks {
    SELF("self", "/users/%s", "Retrieve the current user resource", "GET", false, false),
    USER("user", "/users/%s", "Retrieve a specific user resource", "GET", true, false),
    ACTIVATE("activate", "/users/%s/active", "Activate a user account", "PATCH", false, false),
    CREATE("create", "/users", "Create a new user", "POST", false, false),
    UPDATE("update", "/users/%s", "Update user details", "PATCH", true, false),
    DELETE("delete", "/users/%s", "Delete a user account", "DELETE", true, false);

    private final String rel;
    private final String href;
    private final String title;
    private final String method;
    private final boolean requiresAuth;
    private final boolean templated;
}
