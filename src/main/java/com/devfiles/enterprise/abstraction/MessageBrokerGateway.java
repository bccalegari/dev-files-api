package com.devfiles.enterprise.abstraction;

public interface MessageBrokerGateway {
    void send(String exchange, String routingKey, Object payload);
}
