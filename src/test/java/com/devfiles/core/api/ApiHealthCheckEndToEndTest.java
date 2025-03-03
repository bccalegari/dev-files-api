package com.devfiles.core.api;

import com.devfiles.AbstractTestContainersTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ApiHealthCheckEndToEndTest extends AbstractTestContainersTest {
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