package com.devfiles.core.user.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class UpdateUserRequestDto {
    @Schema(description = "User email. Must be a valid email address.", example = "string")
    @Email(message = "The email field must be a valid email address.")
    private String email;

    @Schema(description = "Username. Must contain at least 3 characters and at most 50 characters.", example = "string")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    @Schema(description = "User password. Must contain at least one uppercase letter, one lowercase letter and one number.",
            example = "string")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one number.")
    @Setter
    private String password;

    @Schema(description = "Password confirmation. Must match the password field.", example = "string")
    @Size(min = 8, max = 50,
            message = "Password confirmation must be between 8 and 50 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Password confirmation must contain at least one uppercase letter, one lowercase letter, and one number.")
    @JsonProperty("password_confirmation")
    private String passwordConfirmation;
}