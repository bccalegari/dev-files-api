package com.devfiles.enterprise.infrastructure.adapter.dto;

import com.devfiles.enterprise.domain.constant.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@JsonPropertyOrder({"metadata", "data", "links", "error"})
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response object")
public class ResponseDto<T> {
    @Schema(description = "Response metadata")
    private Metadata metadata;
    @Schema(description = "Response data")
    private T data;
    @Schema(description = "Response links")
    @JsonProperty("_links")
    private List<Link> links;
    @Schema(description = "Response error")
    private Error error;

    @Builder(access = AccessLevel.PUBLIC)
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Link {
        @Schema(description = "Link relation")
        private String rel;
        @Schema(description = "Link URL")
        private String href;
        @Schema(description = "Link title")
        private String title;
        @Schema(description = "Link type")
        private String type;
        @Schema(description = "Requires authentication")
        @JsonProperty("requires_auth")
        private Boolean requiresAuth;
        @Schema(description = "Is link templated")
        private Boolean templated;
    }

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
        @Schema(description = "Pagination object")
        private Pagination pagination;
        @Schema(description = "Timestamp of the response")
        private String timestamp;
    }

    @Builder
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Pagination {
        @Schema(description = "Total number of items")
        private Long total;
        @Schema(description = "Page number")
        private Integer page;
        @Schema(description = "Number of items per page")
        private Integer limit;
    }

    @NoArgsConstructor
    public static class Empty {}

    public void createLinks(List<Link> links) {
        this.links = new ArrayList<>();
        this.links.addAll(links);
    }

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
                        .pagination(Pagination.builder()
                                .total(total)
                                .page(page)
                                .limit(limit)
                                .build())
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
