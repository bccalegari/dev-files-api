package com.devfiles.core.user.invitation.infrastructure.adapter.controller;

import com.devfiles.core.user.invitation.application.usecase.ResendUserInvitationUseCase;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiPatchV1;
import com.devfiles.enterprise.infrastructure.adapter.annotation.UserAuthorizationValidator;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users/{user_slug}/invitations")
@Tag(name = "User", description = "Endpoints for user management")
@RequiredArgsConstructor
public class ResendUserInvitationController {
    private final ResendUserInvitationUseCase resendUserInvitationUseCase;

    @ApiPatchV1(
            path = "/resend",
            summary = "Resend user invitation",
            description = "Resend an user invitation by slug",
            tags = {"User"}
    )
    @UserAuthorizationValidator
    public ResponseEntity<ResponseDto<ResponseDto.Empty>> execute(
            @RequestAttribute(name = "logged_in_user_slug") String loggedInUserSlug,
            @PathVariable(value = "user_slug") String userSlug
    ) {
        resendUserInvitationUseCase.execute(userSlug);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ResponseDto.success("User invitation resent successfully")
        );
    }
}
