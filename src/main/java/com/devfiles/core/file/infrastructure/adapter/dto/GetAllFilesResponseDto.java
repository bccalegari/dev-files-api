package com.devfiles.core.file.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetAllFilesResponseDto {
    private final String slug;
    private final String name;
    private final String mimeType;
    @JsonProperty("size_in_bytes")
    private final Long sizeInBytes;
    private final String createdAt;
}
