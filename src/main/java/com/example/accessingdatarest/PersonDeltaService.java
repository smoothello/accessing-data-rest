package com.example.accessingdatarest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    private List<Person> previousPersons = new ArrayList<>();

    public void calculateAndPushDelta() {
        Set<Person> currentPersons = new TreeSet<>(jdbcTemplate.query("SELECT * FROM person", new PersonRowMapper()));

        // Sort the list using the natural order defined in the Person class
        List<Person> sortedCurrentPersons = currentPersons.stream()
                .sorted()
                .collect(Collectors.toList());

        List<Person> newPersonsList = new ArrayList<>();
        List<Person> deletedPersonsList = new ArrayList<>();
        List<Person> updatedPersonsList = new ArrayList<>();

        int i = 0, j = 0;
        while (i < previousPersons.size() && j < sortedCurrentPersons.size()) {
            Person previousPerson = previousPersons.get(i);
            Person currentPerson = sortedCurrentPersons.get(j);

            if (previousPerson.getId().equals(currentPerson.getId())) {
                if (!previousPerson.equals(currentPerson)) {
                    updatedPersonsList.add(currentPerson);
                }
                i++;
                j++;
            } else if (previousPerson.getId() < currentPerson.getId()) {
                deletedPersonsList.add(previousPerson);
                i++;
            } else {
                newPersonsList.add(currentPerson);
                j++;
            }
        }

        while (i < previousPersons.size()) {
            deletedPersonsList.add(previousPersons.get(i++));
        }

        while (j < sortedCurrentPersons.size()) {
            newPersonsList.add(sortedCurrentPersons.get(j++));
        }

        if (!newPersonsList.isEmpty()) {
            jmsTemplate.convertAndSend("personDeltaTopic", "New Persons: " + newPersonsList);
        }
        if (!deletedPersonsList.isEmpty()) {
            jmsTemplate.convertAndSend("personDeltaTopic", "Deleted Persons: " + deletedPersonsList);
        }
        if (!updatedPersonsList.isEmpty()) {
            jmsTemplate.convertAndSend("personDeltaTopic", "Updated Persons: " + updatedPersonsList);
        }

        previousPersons = sortedCurrentPersons;
    }
}
