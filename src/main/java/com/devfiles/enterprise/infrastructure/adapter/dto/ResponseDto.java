package com.devfiles.enterprise.infrastructure.adapter.dto;

import com.devfiles.enterprise.domain.constant.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonPropertyOrder({"metadata", "data", "error"})
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response object")
public class ResponseDto<T> {
    @Schema(description = "Response metadata")
    private Metadata metadata;
    @Schema(description = "Response data")
    private T data;
    @Schema(description = "Response error")
    private Error error;

    @Builder
    @Getter
    public static class Error {
        @Schema(description = "Error code", example = "string")
        private ErrorCode code;
        @Schema(description = "Error message")
        private String message;
    }

    @Builder
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Metadata {
        @Schema(description = "Message")
        private String message;
        @Schema(description = "Total number of items")
        private Long total;
        @Schema(description = "Total number of pages")
        private Integer page;
        @Schema(description = "Number of items per page")
        private Integer limit;
        @Schema(description = "Timestamp of the response")
        private String timestamp;
    }

    @NoArgsConstructor
    public static class Empty {}

    public static <T> ResponseDto<T> success(T data, String message) {
        return ResponseDto.<T>builder()
                .metadata(Metadata.builder()
                        .message(message)
                        .timestamp(LocalDateTime.now().toString())
                        .build())
                .data(data)
                .build();
    }

    public static <T> ResponseDto<T> success(String message) {
        return ResponseDto.<T>builder()
                .metadata(Metadata.builder()
                        .message(message)
                        .timestamp(LocalDateTime.now().toString())
                        .build())
                .build();
    }

    public static <T> ResponseDto<T> success(T data, String message, Long total, Integer page, Integer limit) {
        return ResponseDto.<T>builder()
                .metadata(Metadata.builder()
                        .message(message)
                        .total(total)
                        .page(page)
                        .limit(limit)
                        .timestamp(LocalDateTime.now().toString())
                        .build())
                .data(data)
                .build();
    }

    public static <T> ResponseDto<T> error(ErrorCode code, String message) {
        return ResponseDto.<T>builder()
                .metadata(Metadata.builder()
                        .message("An error occurred")
                        .timestamp(LocalDateTime.now().toString())
                        .build())
                .error(Error.builder()
                        .code(code)
                        .message(message)
                        .build())
                .build();
    }
}
