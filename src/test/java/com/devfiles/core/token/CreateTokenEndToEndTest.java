package com.devfiles.core.token;

import com.devfiles.AbstractTestContainersTest;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

public class CreateTokenEndToEndTest extends AbstractTestContainersTest {
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
