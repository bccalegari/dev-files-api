package com.devfiles.core.api.application.usecase;

import com.devfiles.enterprise.domain.constant.ErrorCode;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HealthCheckUseCase {
    @PersistenceContext
    private final EntityManager entityManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    public ResponseDto<ResponseDto.Empty> execute() {

        if (isDatabaseHealthy() && isCacheHealthy() && isMessageBrokerHealthy()) {
            return ResponseDto.success("Application is running healthy");
        } else {
            return ResponseDto.error(
                    ErrorCode.INTERNAL_SERVER_ERROR, "Application is not running healthy"
            );
        }
    }

    private boolean isDatabaseHealthy() {
        return entityManager.createNativeQuery("SELECT 1").getSingleResult().equals(1);
    }

    private boolean isCacheHealthy() {
        return Objects.equals(
                Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().ping(), "PONG"
        );
    }

    private boolean isMessageBrokerHealthy() {
        try (var connection = rabbitTemplate.getConnectionFactory().createConnection()) {
            return connection.isOpen();
        } catch (Exception e) {
            return false;
        }
    }
}
