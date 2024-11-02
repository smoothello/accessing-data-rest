package com.example.accessingdatarest;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Person.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Person_ {

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String ID = "id";

	
	/**
	 * @see com.example.accessingdatarest.Person#firstName
	 **/
	public static volatile SingularAttribute<Person, String> firstName;
	
	/**
	 * @see com.example.accessingdatarest.Person#lastName
	 **/
	public static volatile SingularAttribute<Person, String> lastName;
	
	/**
	 * @see com.example.accessingdatarest.Person#id
	 **/
	public static volatile SingularAttribute<Person, Long> id;
	
	/**
	 * @see com.example.accessingdatarest.Person
	 **/
	public static volatile EntityType<Person> class_;

}

