package com.devfiles.enterprise.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500),
    BAD_REQUEST(400),
    ALREADY_EXISTS(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    NOT_ACCEPTABLE(406),
    CONFLICT(409),
    UNSUPPORTED_MEDIA_TYPE(415);

    private final int httpStatus;
}
