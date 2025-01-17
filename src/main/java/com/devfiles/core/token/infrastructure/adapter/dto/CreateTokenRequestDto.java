package com.devfiles.core.token.infrastructure.adapter.dto;

import com.devfiles.core.token.infrastructure.adapter.annotation.UsernameOrEmailValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@UsernameOrEmailValidation
public class CreateTokenRequestDto {
    private final String username;

    @Email(message = "Email is invalid")
    private final String email;

    @NotEmpty(message = "Password is required")
    private final String password;
}
