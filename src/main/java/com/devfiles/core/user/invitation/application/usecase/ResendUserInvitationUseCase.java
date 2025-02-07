package com.devfiles.core.user.invitation.application.usecase;

import com.devfiles.core.user.application.service.UserMessageBrokerService;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.invitation.application.service.InvitationService;
import com.devfiles.core.user.invitation.domain.entity.Invitation;
import com.devfiles.core.user.invitation.domain.valueobject.InvitationCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResendUserInvitationUseCase {
    private final UserService userService;
    private final InvitationService invitationService;
    private final UserMessageBrokerService userMessageBrokerService;

    @Transactional
    public void execute(String slug) {
        var user = userService.findBySlug(slug);

        var lastInvitation = invitationService.findLastInvitationByUserId(user.getId());

        if (lastInvitation != null) {
            lastInvitation.delete();
        }

        invitationService.save(lastInvitation);

        var invitation = createInvitation(user);

        userMessageBrokerService.sendInvitationRegistrationMessage(invitation);
    }

    private Invitation createInvitation(User user) {
        var invitation = Invitation.builder()
                .user(user)
                .code(new InvitationCode())
                .build();
        return invitationService.save(invitation);
    }
}
