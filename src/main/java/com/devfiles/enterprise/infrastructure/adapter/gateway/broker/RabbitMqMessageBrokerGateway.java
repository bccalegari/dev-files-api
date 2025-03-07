package com.devfiles.enterprise.infrastructure.adapter.gateway.broker;

import com.devfiles.enterprise.abstraction.MessageBrokerGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Qualifier("rabbitMqMessageBrokerGateway")
@Slf4j
public class RabbitMqMessageBrokerGateway implements MessageBrokerGateway {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void send(String exchange, String routingKey, Object payload) {
        var messageBody = parsePayload(payload);
        var message = createMessage(messageBody);

        log.info("Sending message '{}' with body '{}' to exchange '{}' and routing key '{}'",
                message, messageBody, exchange, routingKey);
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (Exception e) {
            log.error("Error sending message", e);
            throw new RuntimeException(e);
        }
    }

    private String parsePayload(Object message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing message", e);
        }
    }

    private Message createMessage(String messageBody) {
        var messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        return new Message(messageBody.getBytes(), messageProperties);
    }
}
