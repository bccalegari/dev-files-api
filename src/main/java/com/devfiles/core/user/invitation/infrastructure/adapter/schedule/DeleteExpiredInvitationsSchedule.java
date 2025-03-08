package com.devfiles.core.user.invitation.infrastructure.adapter.schedule;

import com.devfiles.core.user.invitation.application.service.InvitationService;
import com.devfiles.enterprise.domain.valueobject.TraceId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
        var traceId = new TraceId(MDC.get(TraceId.TRACE_ID_MDC_KEY));
        traceId.registerMdcLog();
        try {
            log.info("Deleting expired invitations");
            var startTime = System.currentTimeMillis();
            var numberOfDeletedInvitations = invitationService.deleteExpiredInvitations();
            var endTime = System.currentTimeMillis();
            log.info("Deleted {} expired invitations in {} ms", numberOfDeletedInvitations, endTime - startTime);
        } finally {
            MDC.clear();
        }
    }
}
