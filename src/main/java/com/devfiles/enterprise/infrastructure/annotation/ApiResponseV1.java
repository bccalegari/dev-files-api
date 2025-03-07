package com.devfiles.enterprise.infrastructure.annotation;

import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "200",
        description = "OK",
        content = {
                @Content(
                        schema = @Schema(implementation = ResponseDto.class)
                )
        }
)
public @interface ApiResponseV1 {
    @AliasFor(annotation = ApiResponse.class, attribute = "responseCode")
    String responseCode();
    @AliasFor(annotation = ApiResponse.class, attribute = "description")
    String description();
}