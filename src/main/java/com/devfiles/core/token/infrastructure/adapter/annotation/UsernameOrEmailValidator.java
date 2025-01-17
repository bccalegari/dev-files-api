package com.devfiles.core.token.infrastructure.adapter.annotation;

import com.devfiles.core.token.infrastructure.adapter.dto.CreateTokenRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameOrEmailValidator implements ConstraintValidator<UsernameOrEmailValidation, CreateTokenRequestDto> {
    @Override
    public boolean isValid(CreateTokenRequestDto createTokenRequestDto, ConstraintValidatorContext context) {
        if (createTokenRequestDto == null) {
            return true;
        }

        boolean isValid = true;

        var existsUsername = createTokenRequestDto.getUsername() != null && !createTokenRequestDto.getUsername().isEmpty();
        var existsEmail = createTokenRequestDto.getEmail() != null && !createTokenRequestDto.getEmail().isEmpty();

        if (!existsUsername && !existsEmail) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("username")
                    .addConstraintViolation();

            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("email")
                    .addConstraintViolation();

            isValid = false;
        }

        return isValid;
    }
}