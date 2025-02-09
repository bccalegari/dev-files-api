package com.devfiles.core.file.application.exception;

import com.devfiles.enterprise.application.exception.BadRequestException;

import java.io.Serial;

public class FileSizeNotAllowedException extends BadRequestException {
    @Serial
    private static final long serialVersionUID = 8650794548992923715L;

    public FileSizeNotAllowedException() {
        super("File size exceeds the limit");
    }
}