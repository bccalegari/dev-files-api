package com.devfiles.core.user.application.usecase;

import com.devfiles.core.user.application.exception.UserAlreadyActivatedException;
import com.devfiles.core.user.application.service.UserService;
import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.invitation.application.exception.InvalidActivationCodeException;
import com.devfiles.core.user.invitation.application.exception.InvitationNotFoundException;
import com.devfiles.core.user.invitation.application.service.InvitationService;
import com.devfiles.core.user.invitation.domain.entity.Invitation;
import com.devfiles.core.user.invitation.domain.valueobject.InvitationCode;
import com.devfiles.enterprise.domain.valueobject.Slug;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActivateUserUseCaseUnitTest {
    @Mock private UserService userService;
    @Mock private InvitationService invitationService;
    @InjectMocks private ActivateUserUseCase activateUserUseCase;

    @Test
    void shouldActivateUser() {
        var user = User.builder()
                .id(1L)
                .slug(Slug.of("slug"))
                .email("email")
                .active(false)
                .build();

        var invitation = Invitation.builder()
                .code(new InvitationCode("code"))
                .createdAt(LocalDateTime.now())
                .build();

        when(userService.findBySlug(user.getSlug().getValue())).thenReturn(user);
        when(invitationService.findLastInvitationByUserId(user.getId())).thenReturn(invitation);

        var activatedUser = User.builder()
                .id(1L)
                .slug(Slug.of("slug"))
                .email("email")
                .active(true)
                .build();

        var activatedInvitation = Invitation.builder()
                .code(new InvitationCode("code"))
                .createdAt(LocalDateTime.now())
                .consumed(true)
                .build();

        when(userService.activateUser(user)).thenReturn(activatedUser);
        when(invitationService.consume(invitation)).thenReturn(activatedInvitation);

        var response = activateUserUseCase.execute(user.getSlug().getValue(), invitation.getCode().value());

        verify(userService).findBySlug(user.getSlug().getValue());
        verify(invitationService).findLastInvitationByUserId(user.getId());
        verify(userService).activateUser(user);
        verify(invitationService).consume(invitation);

        assertEquals(user.getSlug().getValue(), response.getData().getSlug());
        assertTrue(activatedUser.isActive());
        assertTrue(activatedInvitation.isConsumed());
    }

    @Test
    void shouldThrowUserAlreadyActivatedExceptionWhenUserIsActive() {
        var user = User.builder()
                .id(1L)
                .slug(Slug.of("slug"))
                .email("email")
                .active(true)
                .build();

        when(userService.findBySlug(user.getSlug().getValue())).thenReturn(user);

        assertThrows(UserAlreadyActivatedException.class, () -> activateUserUseCase.execute(user.getSlug().getValue(), "code"));
    }

    @Test
    void shouldThrowInvitationNotFoundExceptionWhenInvitationIsExpired() {
        var user = User.builder()
                .id(1L)
                .slug(Slug.of("slug"))
                .email("email")
                .active(false)
                .build();

        var invitation = Invitation.builder()
                .code(new InvitationCode("code"))
                .createdAt(LocalDateTime.now().minusMinutes(6))
                .build();

        when(userService.findBySlug(user.getSlug().getValue())).thenReturn(user);
        when(invitationService.findLastInvitationByUserId(user.getId())).thenReturn(invitation);

        assertThrows(InvitationNotFoundException.class, () -> activateUserUseCase.execute(user.getSlug().getValue(), "code"));
    }

    @Test
    void shouldThrowInvalidActivationCodeExceptionWhenCodeIsInvalid() {
        var user = User.builder()
                .id(1L)
                .slug(Slug.of("slug"))
                .email("email")
                .active(false)
                .build();

        var invitation = Invitation.builder()
                .code(new InvitationCode("code"))
                .createdAt(LocalDateTime.now())
                .build();

        when(userService.findBySlug(user.getSlug().getValue())).thenReturn(user);
        when(invitationService.findLastInvitationByUserId(user.getId())).thenReturn(invitation);

        assertThrows(InvalidActivationCodeException.class, () -> activateUserUseCase.execute(user.getSlug().getValue(), "invalid-code"));
    }
}