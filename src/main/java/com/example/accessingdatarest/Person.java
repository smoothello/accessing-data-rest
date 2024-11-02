// Person.java
package com.example.accessingdatarest;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Table("person")
@Data
public class Person {
	@Id
	@Column("id")
	private Long id;
	@Column("first_name")
	private String firstName;
	@Column("last_name")
	private String lastName;
	@Column("gender")
	private String gender;
	@Column("address")
	private String address;

	public Person(Long id, String firstName, String lastName, String gender, String address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.address = address;
	}
}
