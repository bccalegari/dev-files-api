package com.devfiles.core.api.infrastructure.adapter.controller;

import com.devfiles.core.api.application.usecase.HealthCheckUseCase;
import com.devfiles.enterprise.infrastructure.adapter.annotation.ApiGetV1;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Api", description = "Endpoints for application management")
@RequiredArgsConstructor
public class ApiHealthController {
    private final HealthCheckUseCase healthCheckUseCase;

    @ApiGetV1(
            path = "/",
            summary = "Health check",
            description = "Check if the application is running",
            tags = {"Api"}
    )
    public ResponseEntity<ResponseDto<ResponseDto.Empty>> health() {
        var response = healthCheckUseCase.execute();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}


