package com.devfiles.enterprise.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500),
    INVALID_REQUEST(400),
    ALREADY_EXISTS(400);

    private final int httpStatus;
}
