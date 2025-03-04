package com.devfiles;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractTestContainersTest {
    protected static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine")
            .withReuse(true)
            .withDatabaseName("devfiles")
            .withUsername("devfiles")
            .withPassword("123")
            .withExposedPorts(5432);

    protected static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>("redis:6-alpine")
            .withReuse(true)
            .withExposedPorts(6379)
            .withEnv("REDIS_PASSWORD", "123");

    protected static final GenericContainer<?> RABBITMQ_CONTAINER = new GenericContainer<>("rabbitmq:4.0-rc-management-alpine")
            .withReuse(true)
            .withExposedPorts(5672)
            .withEnv("RABBITMQ_DEFAULT_USER", "devfiles")
            .withEnv("RABBITMQ_DEFAULT_PASS", "123");

    protected static final LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:latest"))
            .withReuse(true)
            .withServices(LocalStackContainer.Service.S3);

    static {
        POSTGRES_CONTAINER.start();
        REDIS_CONTAINER.start();
        RABBITMQ_CONTAINER.start();
        LOCALSTACK_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);

        registry.add("spring.flyway.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.flyway.password", POSTGRES_CONTAINER::getPassword);

        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
        registry.add("spring.data.redis.password", () -> REDIS_CONTAINER.getEnvMap().get("REDIS_PASSWORD"));

        registry.add("spring.rabbitmq.host", RABBITMQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", () -> RABBITMQ_CONTAINER.getMappedPort(5672).toString());
        registry.add("spring.rabbitmq.username", () -> RABBITMQ_CONTAINER.getEnvMap().get("RABBITMQ_DEFAULT_USER"));
        registry.add("spring.rabbitmq.password", () -> RABBITMQ_CONTAINER.getEnvMap().get("RABBITMQ_DEFAULT_PASS"));

        registry.add("localstack.s3.endpoint", LOCALSTACK_CONTAINER::getEndpoint);
        registry.add("aws.credentials.access-key", LOCALSTACK_CONTAINER::getAccessKey);
        registry.add("aws.credentials.secret-key", LOCALSTACK_CONTAINER::getSecretKey);
        registry.add("aws.region", LOCALSTACK_CONTAINER::getRegion);
    }
}