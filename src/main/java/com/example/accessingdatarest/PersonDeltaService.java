package com.example.accessingdatarest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private Set<Person> previousPersons = new TreeSet<>();

    public void calculateAndPushDelta() {
        Set<Person> currentPersons = new TreeSet<>(jdbcTemplate.query("SELECT * FROM person", new PersonRowMapper()));

        List<Person> newPersonsList = currentPersons.stream()
                .filter(person -> !previousPersons.contains(person))
                .collect(Collectors.toList());

        List<Person> deletedPersonsList = previousPersons.stream()
                .filter(person -> !currentPersons.contains(person))
                .collect(Collectors.toList());

        List<Person> updatedPersonsList = currentPersons.stream()
                .filter(person -> previousPersons.contains(person) && !previousPersons.contains(person))
                .collect(Collectors.toList());

        if (!newPersonsList.isEmpty()) {
            jmsTemplate.convertAndSend("personDeltaTopic", "New Persons: " + newPersonsList);
        }
        if (!deletedPersonsList.isEmpty()) {
            jmsTemplate.convertAndSend("personDeltaTopic", "Deleted Persons: " + deletedPersonsList);
        }
        if (!updatedPersonsList.isEmpty()) {
            jmsTemplate.convertAndSend("personDeltaTopic", "Updated Persons: " + updatedPersonsList);
        }

        previousPersons = currentPersons;
    }
}
