package com.example.accessingdatarest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PersonDeltaService {

    @Autowired
    private PersonRepository personRepository;

    private List<Person> previousPersons = new ArrayList<>();

    public void calculateAndLogDelta() {
        List<Person> currentPersons = (List<Person>) personRepository.findAll();

        List<Person> newPersonsList = new ArrayList<>();
        List<Person> deletedPersonsList = new ArrayList<>();
        List<Person> updatedPersonsList = new ArrayList<>();

        int i = 0, j = 0;
        while (i < previousPersons.size() && j < currentPersons.size()) {
            Person previousPerson = previousPersons.get(i);
            Person currentPerson = currentPersons.get(j);

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

        while (j < currentPersons.size()) {
            newPersonsList.add(currentPersons.get(j++));
        }

        // Log results
        log.info("New Persons: {}", newPersonsList);
        log.info("Deleted Persons: {}", deletedPersonsList);
        log.info("Updated Persons: {}", updatedPersonsList);

        // Update previousPersons for the next interval
        previousPersons = currentPersons;
    }
}
