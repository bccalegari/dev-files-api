package com.devfiles.core.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ApiHealthCheckEndToEndTest {

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
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnOkWhenHealthCheckIsSuccessful() {
        var response = restTemplate.getForEntity("/api/health", String.class);
        assertEquals(200, response.getStatusCode().value());

        var responseBody = response.getBody();
        var objectMapper = new ObjectMapper();
        try {
            var parsedResponse = responseBody != null ? objectMapper.readTree(responseBody) : null;
            var status = parsedResponse != null ? parsedResponse.get("metadata").get("message").asText() : null;
            assertEquals("Application is running healthy", status);
        } catch (IOException e) {
            fail("An exception was thrown: " + e.getMessage());
        }
    }
}