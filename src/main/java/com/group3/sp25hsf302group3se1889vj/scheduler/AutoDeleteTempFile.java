package com.group3.sp25hsf302group3se1889vj.scheduler;


import com.group3.sp25hsf302group3se1889vj.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoDeleteTempFile {

    private final StorageService storageService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteTempFile() {
        log.info("Auto delete temp file");
        storageService.deleteAllTempFiles();
    }

}
