package com.devfiles.enterprise.infrastructure.adapter.configuration.spring;

import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.application.exception.BadRequestException;
import com.devfiles.enterprise.domain.valueobject.Slug;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenDecoder;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenProvider;
import com.devfiles.enterprise.infrastructure.adapter.configuration.jwt.JwtTokenValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

public class JwtAuthFilterIntegrationTest {
    private static JwtTokenProvider jwtTokenProvider;
    private static JwtTokenDecoder jwtTokenDecoder;
    private static final JwtTokenValidator jwtTokenValidator = new JwtTokenValidator();
    private static User user;

    private static final String secretKey = "secret-key";
    private static final long accessTokenDurationInMs = 5000; // 5 seconds
    private static final long refreshTokenDurationInMs = 10000; // 10 seconds

    @BeforeAll
    public static void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secretKey, accessTokenDurationInMs, refreshTokenDurationInMs);
        jwtTokenDecoder = new JwtTokenDecoder(secretKey);

        user = mock(User.class);
        when(user.getSlug()).thenReturn(mock(Slug.class));
        when(user.getSlug().getValue()).thenReturn("user-slug");
    }

    @Test
    void shouldSetSubjectInRequestAttribute() {
        var jwt = jwtTokenProvider.generateAccessToken(user);

        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwt);
        var response = new MockHttpServletResponse();
        var filterChain = new MockFilterChain();

        var jwtAuthFilter = new JwtAuthFilter(jwtTokenDecoder, jwtTokenValidator);

        try {
            jwtAuthFilter.doFilterInternal(request, response, filterChain);
        } catch (Exception e) {
            fail("An exception was thrown: " + e.getMessage());
        }


        assertEquals("user-slug", request.getAttribute("logged_in_user_slug"));
    }

    @Test
    void shouldNotSetSubjectInRequestAttributeWhenTokenIsMissing() {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var filterChain = new MockFilterChain();

        var jwtAuthFilter = new JwtAuthFilter(jwtTokenDecoder, jwtTokenValidator);

        try {
            jwtAuthFilter.doFilterInternal(request, response, filterChain);
        } catch (Exception e) {
            fail("An exception was thrown: " + e.getMessage());
        }

        assertNull(request.getAttribute("logged_in_user_slug"));
    }

    @Test
    void shouldNotSetSubjectInRequestAttributeWhenTokenIsInvalid() {
        var jwt = jwtTokenProvider.generateAccessToken(user);

        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwt + "invalid");
        var response = new MockHttpServletResponse();
        var filterChain = new MockFilterChain();

        var jwtAuthFilter = new JwtAuthFilter(jwtTokenDecoder, jwtTokenValidator);

        assertThrows(BadRequestException.class,
                () -> jwtAuthFilter.doFilterInternal(request, response, filterChain));

        assertNull(request.getAttribute("logged_in_user_slug"));
    }

    @Test
    void shouldNotSetSubjectInRequestAttributeWhenTokenIsExpired() {
        var jwt = jwtTokenProvider.generateAccessToken(user);

        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwt);
        var response = new MockHttpServletResponse();
        var filterChain = new MockFilterChain();

        var jwtAuthFilter = new JwtAuthFilter(jwtTokenDecoder, jwtTokenValidator);

        try {
            Thread.sleep(accessTokenDurationInMs + 1000);
        } catch (InterruptedException e) {
            fail("An exception was thrown: " + e.getMessage());
        }

        assertThrows(BadRequestException.class,
                () -> jwtAuthFilter.doFilterInternal(request, response, filterChain));

        assertNull(request.getAttribute("logged_in_user_slug"));
    }
}
