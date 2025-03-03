package com.devfiles.core.user.invitation.usecase;

import com.devfiles.core.user.application.exception.UserAlreadyActivatedException;
import com.devfiles.core.user.application.service.UserMessageBrokerService;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.invitation.application.service.InvitationService;
import com.devfiles.core.user.invitation.application.usecase.ResendUserInvitationUseCase;
import com.devfiles.core.user.invitation.domain.entity.Invitation;
import com.devfiles.enterprise.domain.valueobject.Slug;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResendUserInvitationUseCaseUnitTest {
    @Mock private UserService userService;
    @Mock private InvitationService invitationService;
    @Mock private UserMessageBrokerService userMessageBrokerService;
    @InjectMocks private ResendUserInvitationUseCase resendUserInvitationUseCase;

    @Test
    void shouldResendUserInvitation() {
        var user = User.builder().id(1L).slug(Slug.of("slug")).build();
        var lastInvitation = Invitation.builder().build();

        when(userService.findBySlug("slug")).thenReturn(user);
        when(invitationService.findLastInvitationByUserId(user.getId())).thenReturn(lastInvitation);
        when(invitationService.save(any(Invitation.class))).thenReturn(mock(Invitation.class));
        doNothing().when(userMessageBrokerService).sendInvitationRegistrationMessage(any(Invitation.class));

        resendUserInvitationUseCase.execute(user.getSlug().getValue());

        assertNotNull(lastInvitation.getDeletedAt());

        verify(userService).findBySlug(user.getSlug().getValue());
        verify(invitationService).findLastInvitationByUserId(user.getId());
        verify(invitationService, times(2)).save(any(Invitation.class));
        verify(userMessageBrokerService).sendInvitationRegistrationMessage(any(Invitation.class));
    }

    @Test
    void shouldThrowUserAlreadyActivatedExceptionWhenUserIsActive() {
        var user = User.builder().active(true).slug(Slug.of("slug")).build();

        when(userService.findBySlug("slug")).thenReturn(user);

        assertThrows(UserAlreadyActivatedException.class,
                () -> resendUserInvitationUseCase.execute(user.getSlug().getValue()));

        verify(userService).findBySlug(user.getSlug().getValue());
        verifyNoInteractions(invitationService);
    }
}
