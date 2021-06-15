package com.bala.nosql.mongodb.simplecrudsamples.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bala.nosql.mongodb.simplecrudsamples.entity.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String>{
	public Employee findByFirstName(String firstName);
	public List<Employee> findByLastName(String lastName);
}
