package com.devfiles.enterprise.infrastructure.adapter.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiV1(method = RequestMethod.DELETE, consumes = {}, responseCode = "204", responseDescription = "No content")
public @interface ApiDeleteV1 {
    @AliasFor(annotation = ApiV1.class, attribute = "path")
    String path() default "";

    @AliasFor(annotation = ApiV1.class, attribute = "responseCode")
    String responseCode() default "204";

    @AliasFor(annotation = ApiV1.class, attribute = "responseDescription")
    String responseDescription() default "No content";

    @AliasFor(annotation = ApiV1.class, attribute = "summary")
    String summary();

    @AliasFor(annotation = ApiV1.class, attribute = "description")
    String description();

    @AliasFor(annotation = ApiV1.class, attribute = "tags")
    String[] tags();

    @AliasFor(annotation = ApiV1.class, attribute = "content")
    Content[] content() default {};
}