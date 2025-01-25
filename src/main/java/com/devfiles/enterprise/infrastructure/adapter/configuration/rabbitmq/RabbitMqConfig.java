package com.devfiles.enterprise.infrastructure.adapter.configuration.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRabbit
@Slf4j
public class RabbitMqConfig {
    private final int MAX_ATTEMPTS;
    private final long INITIAL_INTERVAL_IN_MS;
    private final double MULTIPLIER;
    private final long MAX_INTERVAL_IN_MS;

    public RabbitMqConfig(
            @Value("${spring.rabbitmq.template.retry.max-attempts}") int maxAttempts,
            @Value("${spring.rabbitmq.template.retry.initial-interval}") long initialInterval,
            @Value("${spring.rabbitmq.template.retry.multiplier}") double multiplier,
            @Value("${spring.rabbitmq.template.retry.max-interval}") long maxInterval
    ) {
        this.MAX_ATTEMPTS = maxAttempts;
        this.INITIAL_INTERVAL_IN_MS = initialInterval;
        this.MULTIPLIER = multiplier;
        this.MAX_INTERVAL_IN_MS = maxInterval;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        var retryTemplate = RetryTemplate.builder()
                .maxAttempts(MAX_ATTEMPTS)
                .exponentialBackoff(INITIAL_INTERVAL_IN_MS, MULTIPLIER, MAX_INTERVAL_IN_MS)
                .build();
        retryTemplate.setListeners(new RetryListener[]{new RetryListener() {
            @Override
            public <T, E extends Throwable> boolean open(
                    RetryContext context, RetryCallback<T, E> callback
            ) {
                log.info(
                        "Retrying message: {} on attempt: {}",
                        context.getAttribute("message"), context.getRetryCount()
                );
                return true;
            }

            @Override
            public <T, E extends Throwable> void close(
                    RetryContext context, RetryCallback<T, E> callback, Throwable throwable
            ) {
                if (throwable != null) {
                    log.error(
                            "Failed to process message: {} after {} attempts",
                            context.getAttribute("message"), context.getRetryCount()
                    );
                } else {
                    log.info(
                            "Successfully processed message: {} after {} attempts",
                            context.getAttribute("message"), context.getRetryCount()
                    );
                }
            }

            @Override
            public <T, E extends Throwable> void onError(
                    RetryContext context, RetryCallback<T, E> callback, Throwable throwable
            ) {
                log.error(
                        "Error processing message: {} on attempt: {}",
                        context.getAttribute("message"), context.getRetryCount()
                );
            }
        }});
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));
        return retryTemplate;
    }
}