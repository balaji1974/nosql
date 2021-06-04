package com.bala.nosql.redis.springbootsample;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bala.nosql.redis.springbootsample.entity.Student;
import com.bala.nosql.redis.springbootsample.repository.StudentRepository;

@Component
public class RedisOperations {
	
	@Autowired
	StudentRepository studentRepository;
	
	public void saveStudents() {
		Student student = new Student(
				  "Eng2015001", "John Doe", Student.Gender.MALE, 1);
		studentRepository.save(student);
		
		Student engStudent = new Student(
				  "Eng2015002", "John Doe", Student.Gender.MALE, 1);
		Student medStudent = new Student(
		  "Med2015003", "Gareth Houston", Student.Gender.MALE, 2);
		studentRepository.save(engStudent);
		studentRepository.save(medStudent);
	}
	
	public void findAllStudents() {
		List<Student> students = new ArrayList<>();
		studentRepository.findAll().forEach(students::add);
		students.forEach(System.out::println);
	}
	
	public void findStudent() {
		Student student = 
				  studentRepository.findById("Eng2015001").get();
		System.out.println(student);
	}

	public void updateStudent() {
		Student student = 
				  studentRepository.findById("Eng2015001").get();
		student.setName("Richard Watson");
		studentRepository.save(student);
		System.out.println(student);
	}
	
	public void deleteStudent() {
		Student student = 
				  studentRepository.findById("Eng2015001").get();
		studentRepository.deleteById(student.getId());
	}
}
