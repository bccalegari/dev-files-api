package com.devfiles.core.user.infrastructure.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateUserRequestDto {
    @Schema(description = "User email. Must be a valid email address.", example = "string")
    @NotEmpty(message = "Email field is required.")
    @Email(message = "The email field must be a valid email address.")
    private String email;

    @Schema(description = "Username. Must contain at least 3 characters and at most 50 characters.", example = "string")
    @NotEmpty(message = "Username is required.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    @Schema(description = "User password. Must contain at least one uppercase letter, one lowercase letter and one number.",
            example = "string")
    @NotEmpty(message = "Password field is required.")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one number.")
    private String password;

    @Schema(description = "Password confirmation. Must match the password field.", example = "string")
    @Size(min = 8, max = 50,
            message = "Password confirmation must be between 8 and 50 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Password confirmation must contain at least one uppercase letter, one lowercase letter, and one number.")
    @NotEmpty(message = "Password confirmation field is required.")
    private String passwordConfirmation;
}