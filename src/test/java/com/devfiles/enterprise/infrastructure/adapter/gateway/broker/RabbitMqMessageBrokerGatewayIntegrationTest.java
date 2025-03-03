package com.devfiles.enterprise.infrastructure.adapter.gateway.broker;

import com.devfiles.AbstractTestContainersTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RabbitMqMessageBrokerGatewayIntegrationTest extends AbstractTestContainersTest {
    @Autowired
    private RabbitMqMessageBrokerGateway rabbitMqMessageBrokerGateway;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;

    private final String exchangeName = "test-exchange";
    private final String routingKey = "test-routing-key";
    private final String queueName = "test-queue";

    @BeforeEach
    void setUp() {
        var rabbitAdmin = new RabbitAdmin(connectionFactory);

        Exchange exchange = new ExchangeBuilder(exchangeName, "direct").build();
        rabbitAdmin.declareExchange(exchange);

        Queue queue = new Queue(queueName, true);
        rabbitAdmin.declareQueue(queue);

        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
        rabbitAdmin.declareBinding(binding);
    }

    @Test
    void shouldSendAMessageToRabbitMq() {
        var message = "Hello, RabbitMQ!";

        rabbitMqMessageBrokerGateway.send(exchangeName, routingKey, message);
        var receivedMessage = rabbitTemplate.receive(queueName);

        assertNotNull(receivedMessage);
        assertEquals(message, new String(receivedMessage.getBody()).replaceAll("\"", ""));
    }
}