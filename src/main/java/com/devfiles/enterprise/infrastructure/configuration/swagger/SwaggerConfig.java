package com.devfiles.enterprise.infrastructure.configuration.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer addDefaultResponses() {
        return openApi -> openApi.getPaths()
                .forEach((path, pathItem) -> pathItem.readOperations()
                        .forEach(operation -> {
                            var responses = operation.getResponses();
                            if (responses.get("201") == null) {
                                responses.putIfAbsent("200", createApiResponse("OK"));
                            }
                            responses.putIfAbsent("400", createApiResponse("Bad request"));
                            responses.putIfAbsent("401", createApiResponse("Unauthorized"));
                            responses.putIfAbsent("403", createApiResponse("Forbidden"));
                            responses.putIfAbsent("500", createApiResponse("Internal server error"));
                        }));
    }

    @Bean
    public OpenApiCustomizer sortTagsAlphabetically() {
        return openApi -> openApi.setTags(openApi.getTags()
                .stream()
                .sorted(Comparator.comparing(tag -> StringUtils.stripAccents(tag.getName())))
                .toList());
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("dev-files-api")
                        .version("1.0")
                )
                .addSecurityItem(new SecurityRequirement().addList("auth_token"))
                .schemaRequirement("auth_token", createSecurityScheme());
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name("auth_token")
                .scheme("Bearer")
                .bearerFormat("JWT")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER);
    }

    private ApiResponse createApiResponse(String message) {
        return new ApiResponse()
                .description(message)
                .content(new Content()
                        .addMediaType("application/vnd.devfiles.v1+json", new MediaType()
                                .schema(new Schema<>().$ref("#/components/schemas/ResponseDto")))
                        .addMediaType("application/vnd.devfiles.v1+xml", new MediaType()
                                .schema(new Schema<>().$ref("#/components/schemas/ResponseDto")))
                );
    }
}
