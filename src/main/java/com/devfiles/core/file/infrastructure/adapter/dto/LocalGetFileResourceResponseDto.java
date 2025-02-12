package com.devfiles.core.file.infrastructure.adapter.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;

@Builder
@Getter
@Profile("local")
public class LocalGetFileResourceResponseDto {
    private final String fileNameWithExtension;
    @Getter(value = AccessLevel.NONE) private final String mimeType;
    private final UrlResource urlResource;

    public MediaType getMediaType() {
        try {
            return MediaType.valueOf(mimeType);
        } catch (IllegalArgumentException e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
