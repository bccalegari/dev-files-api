package com.devfiles.core.token.infrastructure.adapter.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UsernameOrEmailValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameOrEmailValidation {
    String message() default "Username or email are required";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
