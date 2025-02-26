package com.devfiles.core.token;

import com.devfiles.core.token.infrastructure.adapter.dto.CreateTokenRequestDto;
import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import com.devfiles.core.user.infrastructure.adapter.database.repository.UserRepository;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenDecoder;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CreateTokenEndToEndTest {

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

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JwtTokenDecoder jwtTokenDecoder;
    private JwtTokenValidator jwtTokenValidator;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @BeforeEach
    void setUp() {
        var newUserEntity = UserEntity.builder()
                .slug("user-slug")
                .username("user-username")
                .email("user-email")
                .password(bCryptPasswordEncoder.encode("user-password"))
                .active(true)
                .build();

        userRepository.saveAndFlush(newUserEntity);

        jwtTokenDecoder = new JwtTokenDecoder(secretKey);
        jwtTokenValidator = new JwtTokenValidator();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnTokensWhenUserIsValid() {
        var createTokenRequestDto = new CreateTokenRequestDto(
                "user-username", null, "user-password"
        );

        var response = restTemplate.postForEntity("/tokens", createTokenRequestDto, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        try {
            var responseDto = objectMapper.readTree(response.getBody());
            var data = responseDto.get("data");
            var accessToken = data.get("access_token").asText();
            var refreshToken = data.get("refresh_token").asText();

            assertNotNull(accessToken);
            assertNotNull(refreshToken);

            var decodedAccessToken = jwtTokenDecoder.execute(accessToken);
            var decodedRefreshToken = jwtTokenDecoder.execute(refreshToken);

            assertEquals("user-slug", decodedAccessToken.getSubject());
            assertEquals("user-slug", decodedRefreshToken.getSubject());

            assertTrue(jwtTokenValidator.execute(decodedAccessToken));
            assertTrue(jwtTokenValidator.execute(decodedRefreshToken));
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void shouldReturnBadRequestWhenUserNotFound() {
        var createTokenRequestDto = new CreateTokenRequestDto(
                "invalid-username", null, "user-password"
        );

        var response = restTemplate.postForEntity("/tokens", createTokenRequestDto, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsInvalid() {
        var createTokenRequestDto = new CreateTokenRequestDto(
                "user-username", null, "invalid-password"
        );

        var response = restTemplate.postForEntity("/tokens", createTokenRequestDto, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnUnauthorizedWhenUserIsNotActive() {
        var newUserEntity = UserEntity.builder()
                .slug("inactive-user-slug")
                .username("inactive-user-username")
                .email("inactive-user-email")
                .password(bCryptPasswordEncoder.encode("inactive-user-password"))
                .active(false)
                .build();

        userRepository.saveAndFlush(newUserEntity);

        var createTokenRequestDto = new CreateTokenRequestDto(
                "inactive-user-username", null, "inactive-user-password"
        );

        var response = restTemplate.postForEntity("/tokens", createTokenRequestDto, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
