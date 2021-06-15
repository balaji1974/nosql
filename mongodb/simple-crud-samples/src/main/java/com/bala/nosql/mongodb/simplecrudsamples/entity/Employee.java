package com.bala.nosql.mongodb.simplecrudsamples.entity;

import org.springframework.data.annotation.Id;


// Mongodb maps this to employee collection and if we want to change it 
// we must use @Document annotation to specify the collection name
public class Employee {
	
	// id fits the standard name for a MongoDB ID, 
	// so it does not require any special annotation to tag it for Spring Data MongoDB
	@Id
	private String id;

	private String firstName;
	private String lastName;
	
	public Employee() {
		super();
	}

	public Employee(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return String.format(
		        "Customer[id=%s, firstName='%s', lastName='%s']",
		        id, firstName, lastName);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
