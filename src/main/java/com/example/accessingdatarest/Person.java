package com.example.accessingdatarest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class Person implements Comparable<Person> {
	private Long id;
	@NonNull private String firstName;
	@NonNull private String lastName;
	private String gender;
	private String address;

	@Override
	public int compareTo(Person other) {
		int lastNameComparison = this.lastName.compareTo(other.lastName);
		if (lastNameComparison != 0) {
			return lastNameComparison;
		}
		return this.firstName.compareTo(other.firstName);
	}
}
