package com.devfiles.core.user.application.usecase;

import com.devfiles.core.user.application.exception.UserAlreadyExistsException;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.infrastructure.adapter.dto.UpdateUserRequestDto;
import com.devfiles.core.user.infrastructure.adapter.dto.UpdateUserResponseDto;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Transactional
    public ResponseDto<UpdateUserResponseDto> execute(String userSlug, UpdateUserRequestDto updateUserRequestDto) {
        var user = userService.findBySlug(userSlug);

        updateUserDomain(user, updateUserRequestDto);

        user = userService.save(user);

        var updateUserResponseDto = UpdateUserResponseDto.builder()
                .slug(user.getSlug().getValue())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return ResponseDto.success(updateUserResponseDto, "User updated successfully");
    }

    private void updateUserDomain(User user, UpdateUserRequestDto updateUserRequestDto) {
        updateUsername(user, updateUserRequestDto);
        updateEmail(user, updateUserRequestDto);
        updatePassword(user, updateUserRequestDto);

        user.update();
    }

    private void updateUsername(User user, UpdateUserRequestDto updateUserRequestDto) {
        if (updateUserRequestDto.getUsername() != null) {
            var usernameAlreadyExists = userService.existsByUsername(updateUserRequestDto.getUsername());

            if (usernameAlreadyExists) {
                throw new UserAlreadyExistsException("Username already exists");
            }

            user.setUsername(updateUserRequestDto.getUsername());
        }
    }

    private void updateEmail(User user, UpdateUserRequestDto updateUserRequestDto) {
        if (updateUserRequestDto.getEmail() != null) {
            var emailAlreadyExists = userService.existsByEmail(updateUserRequestDto.getEmail());

            if (emailAlreadyExists) {
                throw new UserAlreadyExistsException("Email already exists");
            }

            user.setEmail(updateUserRequestDto.getEmail());
        }
    }

    private void updatePassword(User user, UpdateUserRequestDto updateUserRequestDto) {
        if (updateUserRequestDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserRequestDto.getPassword()));
        }
    }
}
