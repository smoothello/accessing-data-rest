package com.example.accessingdatarest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class PersonModificationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Random random = new Random();

    private final List<String> firstNames = Arrays.asList("John", "Jane", "Jim", "Alice", "Bob");
    private final List<String> middleNames = Arrays.asList("Michael", "Elizabeth", "Robert", "Marie", "James");
    private final List<String> lastNames = Arrays.asList("Doe", "Smith", "Brown", "Johnson", "Williams");

    public void modifyPersonTable() {
        List<Person> persons = jdbcTemplate.query("SELECT * FROM person", new PersonRowMapper());

        int action = random.nextInt(3);

        switch (action) {
            case 0: // Insert a new person
                String firstName = firstNames.get(random.nextInt(firstNames.size()));
                String middleName = middleNames.get(random.nextInt(middleNames.size()));
                String lastName = lastNames.get(random.nextInt(lastNames.size()));
                String gender = random.nextBoolean() ? "Male" : "Female";
                String address = "Random Address";

                // Check if the person already exists
                int count = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM person WHERE first_name = ? AND middle_name = ? AND last_name = ?",
                        Integer.class, firstName, middleName, lastName);

                if (count == 0) {
                    jdbcTemplate.update("INSERT INTO person (first_name, middle_name, last_name, gender, address) VALUES (?, ?, ?, ?, ?)",
                            firstName, middleName, lastName, gender, address);
                    log.info("Inserted a new person");
                } else {
                    log.info("Person with the same primary key already exists");
                }
                break;
            case 1: // Update an existing person
                if (!persons.isEmpty()) {
                    Person personToUpdate = persons.get(random.nextInt(persons.size()));
                    String updatedGender = random.nextBoolean() ? "Male" : "Female";
                    jdbcTemplate.update("UPDATE person SET address = ?, gender = ? WHERE first_name = ? and middle_name = ? and last_name = ?",
                            "Updated Address " + random.nextInt(100), updatedGender, personToUpdate.getFirstName(), personToUpdate.getMiddleName(), personToUpdate.getLastName());
                    log.info("Updated: {}", personToUpdate);
                }
                break;
            case 2: // Delete an existing person
                if (!persons.isEmpty()) {
                    Person personToDelete = persons.get(random.nextInt(persons.size()));
                    jdbcTemplate.update("DELETE FROM person WHERE first_name = ? and middle_name = ? and last_name = ?",
                            personToDelete.getFirstName(), personToDelete.getMiddleName(), personToDelete.getLastName());
                    log.info("Deleted: {}", personToDelete);
                }
                break;
        }
    }
}
