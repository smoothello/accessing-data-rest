package com.example.accessingdatarest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class PersonModificationService {

    @Autowired
    private PersonRepository personRepository;

    private final Random random = new Random();

    public void modifyPersonTable() {
        List<Person> persons = (List<Person>) personRepository.findAll();

        // Randomly decide to insert, update, or delete a person
        int action = random.nextInt(3);

        switch (action) {
            case 0: // Insert a new person
                Person newPerson = new Person(null, "New", "Person", "Unknown", "Random Address");
                personRepository.save(newPerson);
                log.info("Inserted: {}", newPerson);
                break;
            case 1: // Update an existing person
                if (!persons.isEmpty()) {
                    Person personToUpdate = persons.get(random.nextInt(persons.size()));
                    personToUpdate.setAddress("Updated Address " + random.nextInt(100));
                    personRepository.save(personToUpdate);
                    log.info("Updated: {}", personToUpdate);
                }
                break;
            case 2: // Delete an existing person
                if (!persons.isEmpty()) {
                    Person personToDelete = persons.get(random.nextInt(persons.size()));
                    personRepository.delete(personToDelete);
                    log.info("Deleted: {}", personToDelete);
                }
                break;
        }
    }
}
