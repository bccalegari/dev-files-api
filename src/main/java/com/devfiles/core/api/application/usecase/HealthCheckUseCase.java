package com.devfiles.core.api.application.usecase;

import com.devfiles.enterprise.domain.constant.ErrorCode;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckUseCase {
    @PersistenceContext
    private final EntityManager entityManager;

    public ResponseDto<ResponseDto.Empty> execute() {
        var isDatabaseHealthy = entityManager.createNativeQuery("SELECT 1").getSingleResult();

        if (isDatabaseHealthy.equals(1)) {
            return ResponseDto.<ResponseDto.Empty>success("Application is running healthy");
        } else {
            return ResponseDto.<ResponseDto.Empty>error(
                    ErrorCode.INTERNAL_SERVER_ERROR, "Database is not healthy"
            );
        }
    }
}
