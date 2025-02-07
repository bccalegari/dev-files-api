package com.devfiles.core.user.invitation.infrastructure.adapter.schedule;

import com.devfiles.core.user.invitation.application.service.InvitationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteExpiredInvitationsSchedule {
    private final InvitationService invitationService;

    @Scheduled(cron = "0 0/5 * * * *")
    @Transactional
    public void execute() {
        log.info("Deleting expired invitations");
        var numberOfDeletedInvitations = invitationService.deleteExpiredInvitations();
        log.info("Deleted {} expired invitations", numberOfDeletedInvitations);
    }
}
