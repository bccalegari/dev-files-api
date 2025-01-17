package com.devfiles.enterprise.domain.valueobject;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Slug {
    private final String value;

    public Slug() {
        value = generateValue();
    }

    private Slug(String value) {
        this.value = value;
    }

    public static Slug of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Slug value cannot be null or empty");
        }

        return new Slug(value);
    }

    private String generateValue() {
        return System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
    }
}
