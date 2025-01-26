package com.devfiles.enterprise.infrastructure.adapter.annotation;

import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping
@Operation
@ApiResponse
public @interface ApiV1 {
    @AliasFor(annotation = RequestMapping.class, attribute = "consumes")
    String[] consumes() default {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE};

    @AliasFor(annotation = RequestMapping.class, attribute = "produces")
    String[] produces() default {"application/vnd.devfiles.v1+json", "application/vnd.devfiles.v1+xml"};

    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String path() default "";

    @AliasFor(annotation = RequestMapping.class, attribute = "method")
    RequestMethod method();

    @AliasFor(annotation = Operation.class, attribute = "summary")
    String summary() default "";

    @AliasFor(annotation = Operation.class, attribute = "description")
    String description() default "";

    @AliasFor(annotation = Operation.class, attribute = "tags")
    String[] tags() default {};

    @AliasFor(annotation = ApiResponse.class, attribute = "responseCode")
    String responseCode();

    @AliasFor(annotation = ApiResponse.class, attribute = "description")
    String responseDescription();

    @AliasFor(annotation = ApiResponse.class, attribute = "content")
    Content[] content() default {@Content(
            schema = @Schema(implementation = ResponseDto.class)
    )};
}