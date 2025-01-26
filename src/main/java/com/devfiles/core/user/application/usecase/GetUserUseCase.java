package com.devfiles.core.user.application.usecase;

import com.devfiles.core.user.application.exception.UserNotAllowedToGetAnotherUserException;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.infrastructure.adapter.dto.GetUserResponseDto;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserUseCase {
    private final UserService userService;

    public ResponseDto<GetUserResponseDto> execute(String loggedInUserSlug, String slug) {
        if (!isUserAllowedToGetUser(loggedInUserSlug, slug)) {
            throw new UserNotAllowedToGetAnotherUserException();
        }

        var user = userService.findBySlug(slug);

        var getUserResponseDto = GetUserResponseDto.builder()
            .slug(user.getSlug().getValue())
            .email(user.getEmail())
            .username(user.getUsername())
            .active(user.isActive())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();

        return ResponseDto.success(getUserResponseDto, "User found successfully");
    }

    private boolean isUserAllowedToGetUser(String loggedInUserSlug, String userSlug) {
        return loggedInUserSlug.equals(userSlug);
    }
}
