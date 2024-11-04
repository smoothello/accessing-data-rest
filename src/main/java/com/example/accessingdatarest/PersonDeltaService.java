package com.example.accessingdatarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import jakarta.jms.Message;
import java.util.*;
import org.apache.commons.lang3.tuple.Pair;

@Service
@Slf4j
public class PersonDeltaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private Set<Person> previousPersons = new TreeSet<>();

    public void calculateAndPushDelta() {
        Set<Person> currentPersons = new TreeSet<>(jdbcTemplate.query("SELECT * FROM person", new PersonRowMapper()));

        List<Person> newPersonsList = currentPersons.stream()
                .filter(person -> !previousPersons.contains(person))
                .toList();

        List<Person> deletedPersonsList = previousPersons.stream()
                .filter(person -> !currentPersons.contains(person))
                .toList();

        List<Person> updatedPersonsList = currentPersons.stream()
                .filter(person -> previousPersons.contains(person) && !previousPersons.contains(person))
                .toList();

        List<Pair<List<Person>, PersonDeltaType>> deltaList = new ArrayList<>();
        deltaList.add(Pair.of(newPersonsList, PersonDeltaType.NEW));
        deltaList.add(Pair.of(deletedPersonsList, PersonDeltaType.DELETED));
        deltaList.add(Pair.of(updatedPersonsList, PersonDeltaType.UPDATED));

        deltaList.forEach(pair -> {
            List<Person> list = pair.getLeft();
            PersonDeltaType type = pair.getRight();
            if (!list.isEmpty()) {
                try {
                    String jsonMessage = objectMapper.writeValueAsString(list);
                    sendMessage("/topic/personDelta", jsonMessage, type);
                    //simpMessagingTemplate.convertAndSend("/topic/personDelta", jsonMessage);
                } catch (JsonProcessingException e) {
                    log.error("Error converting list to JSON", e);
                }
            }
        });

        previousPersons = currentPersons;
    }

    private void sendMessage(String destination, String message, PersonDeltaType type) {
        jmsTemplate.send(destination, session -> {
            Message msg = session.createTextMessage(message);
            msg.setStringProperty("type", type.name());
            return msg;
        });
    }
}
