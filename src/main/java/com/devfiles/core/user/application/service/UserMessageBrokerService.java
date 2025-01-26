package com.devfiles.core.user.application.service;

import com.devfiles.core.user.domain.User;
import com.devfiles.core.user.infrastructure.adapter.dto.UserInvitationRegistrationMessageDto;
import com.devfiles.enterprise.abstraction.MessageBrokerGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserMessageBrokerService {
    private final MessageBrokerGateway messageBrokerGateway;
    private final String INVITATION_REGISTRATION_EXCHANGE;
    private final String INVITATION_REGISTRATION_ROUTING_KEY;

    public UserMessageBrokerService(
            @Qualifier("rabbitMqMessageBrokerGateway") MessageBrokerGateway messageBrokerGateway,
            @Value("${message.broker.registration-invite-exchange}") String INVITATION_REGISTRATION_EXCHANGE,
            @Value("${message.broker.registration-invite-key}") String INVITATION_REGISTRATION_ROUTING_KEY
    ) {
        this.messageBrokerGateway = messageBrokerGateway;
        this.INVITATION_REGISTRATION_EXCHANGE = INVITATION_REGISTRATION_EXCHANGE;
        this.INVITATION_REGISTRATION_ROUTING_KEY = INVITATION_REGISTRATION_ROUTING_KEY;
    }

    public void sendInvitationRegistrationMessage(User user) {
        var payload = new UserInvitationRegistrationMessageDto(
                user.getSlug().getValue(),
                user.getUsername(),
                user.getEmail()
        );

        messageBrokerGateway.send(
                INVITATION_REGISTRATION_EXCHANGE,
                INVITATION_REGISTRATION_ROUTING_KEY,
                payload
        );
    }
}
