package com.devfiles.core.file.application.service;

import com.devfiles.core.file.application.factory.GetFileUrlStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFileUrlService {
    private final FileService fileService;
    private final GetFileUrlStrategyFactory getFileUrlStrategyFactory;

    public String execute(String slug, Long userId) {
        var file = fileService.findBySlugAndUserId(slug, userId);
        var strategy = getFileUrlStrategyFactory.getFileUrlStrategy();
        return strategy.execute(file);
    }
}
