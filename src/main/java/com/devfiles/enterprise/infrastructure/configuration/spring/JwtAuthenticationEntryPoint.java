package com.devfiles.enterprise.infrastructure.configuration.spring;

import com.devfiles.enterprise.domain.constant.ErrorCode;
import com.devfiles.enterprise.infrastructure.adapter.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException {
        log.error(exception.getMessage(), exception);

        var responseDto = ResponseDto.error(ErrorCode.UNAUTHORIZED,
                "Unauthorized access, please authenticate before proceeding");

        String acceptHeader = request.getHeader("Accept");

        if (acceptHeader.equals("application/vnd.devfiles.v1+xml")) {
            response.setContentType("application/xml");
            response.getWriter().write(convertToXml(responseDto));
        } else {
            response.setContentType("application/json");
            response.getWriter().write(convertToJson(responseDto));
        }
    }

    private String convertToJson(ResponseDto responseDto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(responseDto);
    }

    private String convertToXml(ResponseDto responseDto) throws IOException {
        ObjectMapper xmlMapper = new XmlMapper();
        return xmlMapper.writeValueAsString(responseDto);
    }
}
