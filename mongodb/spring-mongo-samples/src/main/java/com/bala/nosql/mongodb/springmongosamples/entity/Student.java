package com.bala.nosql.mongodb.springmongosamples.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="student")
public class Student {
	
	@Id
	private int id;
	
	// @Field(name="name") // Optional as java name and collection field name are same
	private String name;
	
	//@Field(name="email")
	private String email;
	
	//@Field(name="department")
	private Department department;
	
	//@Field(name="subjects")
	private List<Subject> subjects;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public List<Subject> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
	
	
	
	
}
