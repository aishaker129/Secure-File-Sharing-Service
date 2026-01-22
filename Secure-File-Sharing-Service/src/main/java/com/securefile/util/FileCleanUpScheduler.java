package com.securefile.util;

import com.securefile.service.FileService;
import com.securefile.service.impl.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileCleanUpScheduler {
    private final FileServiceImpl fileService;

    @Scheduled(fixedRate = 60000)
    public void cleanUp(){
        fileService.cleanupExpiredFiles();
    }
}
