package com.example.accessingdatarest;


import java.io.Serializable;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class PersonId implements Serializable {
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;

    public PersonId(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
