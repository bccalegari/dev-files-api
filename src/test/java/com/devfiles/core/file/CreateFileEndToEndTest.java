package com.devfiles.core.file;

import com.devfiles.AbstractTestContainersTest;
import com.devfiles.core.file.application.service.FileEmbeddingListener;
import com.devfiles.core.file.domain.NewFileEvent;
import com.devfiles.core.file.infrastructure.adapter.database.repository.FileRepository;
import com.devfiles.core.token.infrastructure.adapter.dto.CreateTokenRequestDto;
import com.devfiles.core.user.infrastructure.adapter.database.entity.UserEntity;
import com.devfiles.core.user.infrastructure.adapter.database.repository.UserRepository;
import com.devfiles.core.user.invitation.infrastructure.adapter.database.repository.InvitationRepository;
import com.devfiles.enterprise.domain.valueobject.Slug;
import com.devfiles.enterprise.infrastructure.configuration.aws.AwsCredentialsProviderConfiguration;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.util.LinkedMultiValueMap;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

public class CreateFileEndToEndTest extends AbstractTestContainersTest {
    private static WireMockServer wireMockServer;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AwsCredentialsProviderConfiguration credentialsProvider;

    @Autowired
    private Environment environment;

    @MockitoSpyBean
    private FileEmbeddingListener fileEmbeddingListener;

    private UserEntity userEntity;

    @BeforeAll
    static void setUpAll() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(5000)
        );
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        stubFor(post(urlEqualTo("/api/v1/embedding"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-API-KEY", matching(".*"))
                .withRequestBody(matchingJsonPath("$.url"))
                .withRequestBody(matchingJsonPath("$.user_slug"))
                .withRequestBody(matchingJsonPath("$.document_slug"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("X-TRACE-ID", "test")
                        .withBodyFile("ai_service_embedding_file_response.json")));
    }

    @BeforeEach
    void setUp() {
        var s3Client = S3Client.builder()
                .endpointOverride(LOCALSTACK_CONTAINER.getEndpoint())
                .region(Region.of(LOCALSTACK_CONTAINER.getRegion()))
                .credentialsProvider(credentialsProvider.awsCredentialsProvider())
                .build();

        s3Client.createBucket(builder -> builder.bucket(environment.getProperty("aws.s3.bucket-name")));

        userEntity = UserEntity.builder()
                .username("test")
                .email("test@test.com")
                .slug(new Slug().getValue())
                .password(new BCryptPasswordEncoder().encode("test@Test123"))
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        userEntity = userRepository.save(userEntity);
    }

    @AfterEach
    void tearDown() {
        invitationRepository.deleteAll();
        fileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    static void tearDownAll() {
        wireMockServer.stop();
    }

    @Test
    void shouldCreateFile() {
        var objectMapper = new ObjectMapper();

        var createTokenRequestDto = new CreateTokenRequestDto(
                userEntity.getUsername(), null, "test@Test123"
        );

        var tokenResponse = restTemplate.postForEntity("/tokens", createTokenRequestDto, String.class);

        JsonNode tokenResponseBody = null;

        try {
            tokenResponseBody = objectMapper.readTree(tokenResponse.getBody());
        } catch (Exception e) {
            fail("Failed to parse token response body: " + e.getMessage());
        }

        var accessToken = tokenResponseBody.get("data").get("access_token").asText();

        var multipartFile = new MockMultipartFile(
                "file", "file.pdf", "application/pdf", "test content".getBytes()
        );

        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        var response = restTemplate.postForEntity(
                "/users/" + userEntity.getSlug() + "/files",
                new HttpEntity<>(new LinkedMultiValueMap<String, Object>() {{
                    add("file", multipartFile.getResource());
                }}, headers),
                String.class
        );

        assertEquals(201, response.getStatusCode().value());

        JsonNode responseBody = null;

        try {
            responseBody = objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            fail("Failed to parse response body: " + e.getMessage());
        }

        var responseData = responseBody.get("data");

        await().untilAsserted(() ->
                Mockito.verify(fileEmbeddingListener).execute(Mockito.any(NewFileEvent.class))
        );

        var fileOp = fileRepository.findBySlugAndUserIdAndDeletedAtIsNull(
                responseData.get("slug").asText(), userEntity.getId()
        );

        var file = fileOp.orElse(null);

        assertNotNull(file);
        assertEquals(responseData.get("slug").asText(), file.getSlug());
        assertTrue(file.isEmbedded());
    }
}
