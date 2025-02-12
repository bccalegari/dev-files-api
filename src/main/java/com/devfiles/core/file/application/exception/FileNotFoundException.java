package com.devfiles.core.file.application.exception;

import com.devfiles.enterprise.application.exception.NotFoundException;

import java.io.Serial;

public class FileNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = 5461280212000161516L;

    public FileNotFoundException() {
        super("File not found");
    }
}