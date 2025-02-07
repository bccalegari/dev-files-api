package com.devfiles.core.user.invitation.infrastructure.adapter.controller;

import com.devfiles.core.user.invitation.application.usecase.ResendUserInvitationUseCase;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiPatchV1;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users/{slug}/invitations")
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
    public ResponseEntity<ResponseDto<ResponseDto.Empty>> execute(
            @PathVariable(value = "slug") String slug
    ) {
        resendUserInvitationUseCase.execute(slug);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ResponseDto.success("User invitation resent successfully")
        );
    }
}
