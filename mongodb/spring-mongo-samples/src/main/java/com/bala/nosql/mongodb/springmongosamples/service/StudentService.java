package com.bala.nosql.mongodb.springmongosamples.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bala.nosql.mongodb.springmongosamples.entity.Student;
import com.bala.nosql.mongodb.springmongosamples.repository.StudentRepository;

@Service
public class StudentService {
	
	@Autowired 
	StudentRepository studentRepository;

	public Student createStudent(Student student) {
		return studentRepository.save(student);
	}
	
	public Student getStudentById(String id) {
		return studentRepository.findById(id).get();
	}

	public List<Student> getStudents() {
		return studentRepository.findAll();
	}

	public Student updateStudent(Student student) {
		return studentRepository.save(student);
	}
	
	public void deleteStudentById(String id) {
		studentRepository.deleteById(id);
	}
}
