package com.devfiles.enterprise.infrastructure.adapter.gateway.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RedisCacheGatewayIntegrationTest {

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withReuse(true);

    @Container
    public static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:6-alpine")
            .withReuse(true)
            .withExposedPorts(6379)
            .withEnv("REDIS_PASSWORD", "123");

    @Container
    public static final GenericContainer<?> rabbitMQContainer = new GenericContainer<>("rabbitmq:4.0-rc-management-alpine")
            .withReuse(true)
            .withExposedPorts(5672)
            .withEnv("RABBITMQ_DEFAULT_USER", "devfiles")
            .withEnv("RABBITMQ_DEFAULT_PASS", "123");

    @DynamicPropertySource
    static void configureDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379).toString());
        registry.add("spring.data.redis.password", () -> redisContainer.getEnvMap().get("REDIS_PASSWORD"));

        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbitMQContainer.getMappedPort(5672).toString());
        registry.add("spring.rabbitmq.username", () -> rabbitMQContainer.getEnvMap().get("RABBITMQ_DEFAULT_USER"));
        registry.add("spring.rabbitmq.password", () -> rabbitMQContainer.getEnvMap().get("RABBITMQ_DEFAULT_PASS"));
    }

    @Autowired
    private RedisCacheGateway redisCacheGateway;

    @Test
    void shouldPutAndRetrieveValueFromCache() {
        redisCacheGateway.put("key", "value");
        var value = redisCacheGateway.get("key");
        assertEquals("value", value);
    }

    @Test
    void shouldDeleteValueFromCache() {
        redisCacheGateway.put("key", "value");
        redisCacheGateway.delete("key");
        var value = redisCacheGateway.get("key");
        assertNull(value);
    }
}
