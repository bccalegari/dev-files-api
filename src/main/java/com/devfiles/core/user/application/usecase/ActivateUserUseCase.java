package com.devfiles.core.user.application.usecase;

import com.devfiles.core.user.application.exception.UserAlreadyActivatedException;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.infrastructure.adapter.dto.ActivateUserResponseDto;
import com.devfiles.core.user.invitation.application.exception.InvalidActivationCodeException;
import com.devfiles.core.user.invitation.application.exception.InvitationNotFoundException;
import com.devfiles.core.user.invitation.application.service.InvitationService;
import com.devfiles.core.user.invitation.domain.entity.Invitation;
import com.devfiles.enterprise.application.exception.CoreException;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivateUserUseCase {
    private final UserService userService;
    private final InvitationService invitationService;

    @Transactional
    public ResponseDto<ActivateUserResponseDto> execute(String slug, String code) {
        var user = userService.findBySlug(slug);

        if (user.isActive()) {
            throw new UserAlreadyActivatedException();
        }

        Invitation invitation;

        try {
            invitation = invitationService.findLastInvitationByUserId(user.getId());
        } catch (InvitationNotFoundException e) {
            throw new CoreException();
        }

        if (!invitation.getCode().value().equals(code)) {
            throw new InvalidActivationCodeException();
        }

        user = userService.activateUser(user);

        invitationService.consume(invitation);

        var activateUserResponseDto = ActivateUserResponseDto.builder()
                .slug(user.getSlug().getValue())
                .email(user.getEmail())
                .active(user.isActive())
                .updatedAt(user.getUpdatedAt())
                .build();

        return ResponseDto.success(activateUserResponseDto, "User activated successfully");
    }
}
