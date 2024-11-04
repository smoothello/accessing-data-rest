package com.example.accessingdatarest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonDeltaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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

        Map<List<Person>, PersonDeltaType> deltaMap = Map.of(
                newPersonsList, PersonDeltaType.NEW,
                deletedPersonsList, PersonDeltaType.DELETED,
                updatedPersonsList, PersonDeltaType.UPDATED
        );

        deltaMap.forEach((list, type) -> {
            if (!list.isEmpty()) {
                try {
                    sendMessage("personDeltaTopic", objectMapper.writeValueAsString(list), type);
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
