package com.devfiles.core.file.application.exception;

import com.devfiles.enterprise.application.exception.BadRequestException;

import java.io.Serial;

public class FileMimeTypeNotAllowedException extends BadRequestException {
    @Serial
    private static final long serialVersionUID = -638909150941664558L;

    public FileMimeTypeNotAllowedException() {
        super("Invalid file type");
    }
}