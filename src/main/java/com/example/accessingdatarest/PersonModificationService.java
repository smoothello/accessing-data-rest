package com.example.accessingdatarest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class PersonModificationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Random random = new Random();

    public void modifyPersonTable() {
        List<Person> persons = jdbcTemplate.query("SELECT * FROM person", new PersonRowMapper());

        int action = random.nextInt(3);

        switch (action) {
            case 0: // Insert a new person
                String gender = random.nextBoolean() ? "Male" : "Female";
                jdbcTemplate.update("INSERT INTO person (first_name, last_name, gender, address) VALUES (?, ?, ?, ?)",
                        "New", "Person", gender, "Random Address");
                log.info("Inserted a new person");
                break;
            case 1: // Update an existing person
                if (!persons.isEmpty()) {
                    Person personToUpdate = persons.get(random.nextInt(persons.size()));
                    String updatedGender = random.nextBoolean() ? "Male" : "Female";
                    jdbcTemplate.update("UPDATE person SET address = ?, gender = ? WHERE id = ?",
                            "Updated Address " + random.nextInt(100), updatedGender, personToUpdate.getId());
                    log.info("Updated: {}", personToUpdate);
                }
                break;
            case 2: // Delete an existing person
                if (!persons.isEmpty()) {
                    Person personToDelete = persons.get(random.nextInt(persons.size()));
                    jdbcTemplate.update("DELETE FROM person WHERE id = ?", personToDelete.getId());
                    log.info("Deleted: {}", personToDelete);
                }
                break;
        }
    }
}
