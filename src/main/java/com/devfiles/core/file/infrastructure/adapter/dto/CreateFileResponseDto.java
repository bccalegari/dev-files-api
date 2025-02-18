package com.devfiles.core.file.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CreateFileResponseDto {
    private final String slug;

    @JsonProperty("file_name")
    private final String fileName;

    private final String url;

    @JsonProperty("mime_type")
    private final String mimeType;

    @JsonProperty("size_in_bytes")
    private final Long sizeInBytes;

    @JsonProperty("created_at")
    private final LocalDateTime createdAt;
}
