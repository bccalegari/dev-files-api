package com.devfiles.core.file.application.exception;

import com.devfiles.enterprise.application.exception.BadRequestException;

import java.io.Serial;

public class FileCorruptedException extends BadRequestException {
    @Serial
    private static final long serialVersionUID = 349905341828860524L;

    public FileCorruptedException() {
        super("File is corrupted");
    }
}