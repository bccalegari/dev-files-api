package com.devfiles.core.user;

import com.devfiles.AbstractTestContainersTest;
import com.devfiles.core.user.infrastructure.adapter.database.repository.UserRepository;
import com.devfiles.core.user.infrastructure.adapter.dto.CreateUserRequestDto;
import com.devfiles.core.user.invitation.infrastructure.adapter.database.repository.InvitationRepository;
import com.devfiles.enterprise.domain.constant.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

public class CreateUserEndToEndTest extends AbstractTestContainersTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @BeforeAll
    static void setUpAll(
            @Autowired ConnectionFactory connectionFactory
    ) {
        var rabbitAdmin = new RabbitAdmin(connectionFactory);

        var exchangeName = "registration-invite-exchange";
        var exchange = new ExchangeBuilder(exchangeName, "direct").build();
        rabbitAdmin.declareExchange(exchange);

        var queue = new Queue("registration-invite-queue", true);
        rabbitAdmin.declareQueue(queue);

        var routingKey = "registration-invite-key";
        var binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
        rabbitAdmin.declareBinding(binding);
    }

    @AfterEach
    void tearDown() {
        invitationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() {
        var createUserRequestDto = CreateUserRequestDto.builder()
                .email("test@test.com")
                .username("test")
                .password("test@Test123")
                .passwordConfirmation("test@Test123")
                .build();

        var response = restTemplate.postForEntity("/users", createUserRequestDto, String.class);
        assertEquals(201, response.getStatusCode().value());

        JsonNode responseBody = null;
        var objectMapper = new ObjectMapper();

        try {
            responseBody = objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            fail("Failed to parse response body: " + e.getMessage());
        }

        var responseData = responseBody.get("data");

        assertEquals(createUserRequestDto.getEmail(), responseData.get("email").asText());

        var userOp = userRepository.findBySlugAndDeletedAtIsNull(responseData.get("slug").asText());

        var user = userOp.orElse(null);

        assertNotNull(user);

        assertEquals(responseData.get("slug").asText(), user.getSlug());
        assertEquals(createUserRequestDto.getEmail(), user.getEmail());
        assertEquals(createUserRequestDto.getUsername(), user.getUsername());
        assertFalse(user.isActive());

        var invitationOp = invitationRepository.findLastInvitationByUserId(user.getId());
        var invitation = invitationOp.orElse(null);

        assertNotNull(invitation);

        var message = rabbitTemplate.receive("registration-invite-queue");

        var messageBody = new String(message != null ? message.getBody() : new byte[0]);

        assertTrue(messageBody.contains(responseData.get("slug").asText()));
        assertTrue(messageBody.contains(invitation.getCode()));
    }

    @Test
    void shouldReturnBadRequestWhenUserAlreadyExists() {
        var createUserRequestDto = CreateUserRequestDto.builder()
                .email("test@test.com")
                .username("test")
                .password("test@Test123")
                .passwordConfirmation("test@Test123")
                .build();

        restTemplate.postForEntity("/users", createUserRequestDto, String.class);

        var response = restTemplate.postForEntity("/users", createUserRequestDto, String.class);

        assertEquals(400, response.getStatusCode().value());

        JsonNode responseBody = null;
        var objectMapper = new ObjectMapper();

        try {
            responseBody = objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            fail("Failed to parse response body: " + e.getMessage());
        }

        var responseError = responseBody.get("error");

        assertEquals(ErrorCode.ALREADY_EXISTS.toString(), responseError.get("code").asText());
        assertEquals("User already exists", responseError.get("message").asText());
    }
}
