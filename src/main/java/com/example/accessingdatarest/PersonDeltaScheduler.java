package com.example.accessingdatarest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PersonDeltaScheduler {

    @Autowired
    private PersonDeltaService personDeltaService;

    @Scheduled(fixedRate = 60000,initialDelay = 15000) // Every minute
    public void scheduleDeltaCalculationTask() {
        personDeltaService.calculateAndLogDelta();
    }
}
