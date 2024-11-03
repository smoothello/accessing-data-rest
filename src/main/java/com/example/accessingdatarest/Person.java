// Person.java
package com.example.accessingdatarest;

import lombok.Data;

@Data
public class Person {
	private Long id;
	private String firstName;
	private String lastName;
	private String gender;
	private String address;

	public Person(Long id, String firstName, String lastName, String gender, String address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.address = address;
	}
}
