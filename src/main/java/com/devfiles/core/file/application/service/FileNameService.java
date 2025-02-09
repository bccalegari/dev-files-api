package com.devfiles.core.file.application.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;

@Service
public class FileNameService {
    public String execute(String originalFileName) {
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("File name is required");
        }

        int lastDotIndex = originalFileName.lastIndexOf(".");
        String fileName = (lastDotIndex == -1) ? originalFileName : originalFileName.substring(0, lastDotIndex);

        fileName = Normalizer.normalize(fileName, Normalizer.Form.NFD);
        fileName = fileName.replaceAll("[^\\p{ASCII}]", "");

        fileName = fileName.replaceAll("[^a-zA-Z0-9_]", "_");
        
        fileName = fileName.toLowerCase(Locale.ROOT);
        
        fileName = fileName.replaceAll("_+", "_").replaceAll("^_|_$", "");
        
        return fileName;
    }
}
