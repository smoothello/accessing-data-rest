package com.example.accessingdatarest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PersonModificationScheduler {

    @Autowired
    private PersonModificationService personModificationService;

    @Scheduled(fixedDelay = 30000,initialDelay = 30000) // Every minute
    public void scheduleModificationTask() {
        personModificationService.modifyPersonTable();
    }
}
