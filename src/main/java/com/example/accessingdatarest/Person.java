package com.example.accessingdatarest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Comparator;

@Data
@AllArgsConstructor
public class Person implements Comparable<Person> {
	@NonNull private String firstName;
	@NonNull private String middleName;
	@NonNull private String lastName;
	private String gender;
	private String address;

	@Override
	public int compareTo(Person other) {
		return Comparator.comparing(Person::getLastName)
				.thenComparing(Person::getFirstName)
				.thenComparing(Person::getMiddleName)
				.compare(this, other);
	}
}
