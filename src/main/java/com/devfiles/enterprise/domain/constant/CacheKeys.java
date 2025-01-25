package com.devfiles.enterprise.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheKeys {
    USER("user::slug=%s");

    private final String key;
}