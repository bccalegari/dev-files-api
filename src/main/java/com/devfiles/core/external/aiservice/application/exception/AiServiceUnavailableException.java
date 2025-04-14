package com.devfiles.core.external.aiservice.application.exception;

import com.devfiles.enterprise.application.exception.CoreException;

import java.io.Serial;

public class AiServiceUnavailableException extends CoreException {
    @Serial
    private static final long serialVersionUID = -7981176724129716487L;

    public AiServiceUnavailableException() {
        super("We're sorry, but at the moment we're unable to answer your question. Please try again later.");
    }
}