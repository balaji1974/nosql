package com.bala.nosql.mongodb.simplecrudsamples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bala.nosql.mongodb.simplecrudsamples.entity.Employee;
import com.bala.nosql.mongodb.simplecrudsamples.repository.EmployeeRepository;

@SpringBootApplication
public class SimpleCrudSamplesApplication implements CommandLineRunner {

	@Autowired
	private EmployeeRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(SimpleCrudSamplesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		// save a couple of employees
		repository.save(new Employee("Alice", "Smith"));
		repository.save(new Employee("Bob", "Smith"));

		// fetch all employees
		System.out.println("Employees found with findAll():");
		System.out.println("-------------------------------");
		for (Employee employee : repository.findAll()) {
			System.out.println(employee);
		}
		System.out.println();

		// fetch an individual employee
		System.out.println("Employee found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(repository.findByFirstName("Alice"));

		System.out.println("Employee found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		for (Employee employee : repository.findByLastName("Smith")) {
			System.out.println(employee);
		}

	}
}
