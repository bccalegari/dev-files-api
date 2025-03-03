package com.devfiles.core.user.invitation.service;

import com.devfiles.core.user.invitation.abstraction.InvitationRepositoryGateway;
import com.devfiles.core.user.invitation.application.exception.InvitationNotFoundException;
import com.devfiles.core.user.invitation.application.service.InvitationService;
import com.devfiles.core.user.invitation.domain.entity.Invitation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvitationServiceUnitTest {
    @Mock private InvitationRepositoryGateway invitationRepositoryGateway;
    @InjectMocks private InvitationService invitationService;

    @Test
    void shouldFindLastInvitationByUserIdWhenInvitationExists() {
        var invitation = mock(Invitation.class);

        when(invitationRepositoryGateway.findLastInvitationByUserId(1L)).thenReturn(Optional.of(invitation));

        invitationService.findLastInvitationByUserId(1L);

        verify(invitationRepositoryGateway).findLastInvitationByUserId(1L);
    }

    @Test
    void shouldThrowInvitationNotFoundExceptionWhenInvitationDoesNotExist() {
        when(invitationRepositoryGateway.findLastInvitationByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(InvitationNotFoundException.class, () -> invitationService.findLastInvitationByUserId(1L));

        verify(invitationRepositoryGateway).findLastInvitationByUserId(1L);
    }

    @Test
    void shouldConsumeInvitation() {
        var invitation = Invitation.builder().build();

        invitationService.consume(invitation);

        assertTrue(invitation.isConsumed());

        verify(invitationRepositoryGateway).save(invitation);
    }

    @Test
    void shouldDeleteExpiredInvitations() {
        var invitation1 = Invitation.builder().createdAt(LocalDateTime.now().minusMinutes(7)).build();
        var invitation2 = Invitation.builder().createdAt(LocalDateTime.now().minusMinutes(6)).build();

        when(invitationRepositoryGateway.findAll()).thenReturn(List.of(invitation1, invitation2));

        invitationService.deleteExpiredInvitations();

        assertNotNull(invitation1.getDeletedAt());
        assertNotNull(invitation2.getDeletedAt());

        verify(invitationRepositoryGateway).saveAll(List.of(invitation1, invitation2));
    }
}
