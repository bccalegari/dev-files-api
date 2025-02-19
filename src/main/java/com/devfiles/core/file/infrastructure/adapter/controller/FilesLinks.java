package com.devfiles.core.file.infrastructure.adapter.controller;

import com.devfiles.enterprise.infrastructure.adapter.controller.AbstractLinks;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FilesLinks implements AbstractLinks {
    SELF("self", "/users/%s/files/%s", "Retrieve the current file resource", "GET",
            true, false),
    FILE("file", "/users/%s/files/{file_slug}", "Retrieve a specific file resource", "GET",
            true, true),
    FILES("files", "/users/%s/files?page={page}&limit={limit}&search={search}&sort={sort}&sortBy={sortBy}",
            "Retrieve all files for a user", "GET", true, true),
    FILES_PREVIOUS("files_previous", "/users/%s/files?page=%s&limit=%s&search=%s&sort=%s&sortBy=%s",
            "Retrieve the previous page of files", "GET", true, true),
    FILES_NEXT("files_next", "/users/%s/files?page=%s&limit=%s&search=%s&sort=%s&sortBy=%s",
            "Retrieve the next page of files", "GET", true, true),
    CREATE("create", "/users/%s/files", "Create a new file", "POST", true,
            false),
    DELETE("delete", "/users/%s/files/%s", "Delete a specific file", "DELETE", true,
            false);

    private final String rel;
    private final String href;
    private final String title;
    private final String method;
    private final boolean requiresAuth;
    private final boolean templated;
}
