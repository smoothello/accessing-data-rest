package com.example.accessingdatarest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonDeltaListener {

    @JmsListener(destination = "personDeltaTopic", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(String message) {
        log.info("Received message from personDeltaTopic: {}", message);
        // You can also display the message in other ways if needed
    }
}
