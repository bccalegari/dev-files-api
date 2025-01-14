package com.devfiles.enterprise.application.exception;

import com.devfiles.enterprise.domain.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoreException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5071282406606769362L;
    public static final String DEFAULT_MESSAGE = "An error occurred, please try again later.";
    protected String message = DEFAULT_MESSAGE;
    protected Integer httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
    protected ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
}