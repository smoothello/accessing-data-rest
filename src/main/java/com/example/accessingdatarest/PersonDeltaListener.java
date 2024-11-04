package com.example.accessingdatarest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
@Slf4j
public class PersonDeltaListener {

    @Autowired
    private ObjectMapper objectMapper;

    private final Map<PersonDeltaType, Consumer<List<Person>>> actions = Map.of(
        PersonDeltaType.NEW, persons -> log.info("Received new persons list: {}", persons),
        PersonDeltaType.DELETED, persons -> log.info("Received deleted persons list: {}", persons),
        PersonDeltaType.UPDATED, persons -> log.info("Received updated persons list: {}", persons)
    );

    @JmsListener(destination = "/topic/personDelta", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            String text = ((TextMessage) message).getText();
            String typeString = message.getStringProperty("type");
            PersonDeltaType type = PersonDeltaType.valueOf(typeString);

            try {
                List<Person> persons = objectMapper.readValue(text, new TypeReference<List<Person>>() {});
                actions.getOrDefault(type, p -> log.warn("Received unknown message type: {}", text)).accept(persons);
            } catch (Exception e) {
                log.error("Error parsing JSON message", e);
            }
        } else {
            log.warn("Received non-text message");
        }
    }
}
