package com.bala.nosql.mongodb.springmongocloud.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bala.nosql.mongodb.springmongocloud.entity.Student;
import com.bala.nosql.mongodb.springmongocloud.repository.StudentRepository;

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

	public List<Student> getStudentsByName(String name) {
		return studentRepository.findByName(name);
	}

	public List<Student> getStudentsByNameAndEmail(String name, String email) {
		return studentRepository.findByNameAndEmail(name, email);
	}
	
	public List<Student> getStudentsByNameOrEmail(String name, String email) {
		return studentRepository.findByNameOrEmail(name, email);
	}

	public List<Student> getStudentsWithPagination(int pageNo, int pageSize) {
		// Since page no is a zero based index use -1 from whatever pageno is got
		Pageable pageable= PageRequest.of(pageNo-1, pageSize);
		return studentRepository.findAll(pageable).getContent();
		
	}
	
	public List<Student> getStudentsSortByName() {
		Sort sort=Sort.by(Sort.Direction.ASC, "name");
		//Sort sort=Sort.by(Sort.Direction.ASC, "name","_id");
		//Sort sort=Sort.by(Sort.Order.asc("name"),Sort.Order.desc("_id")); // To sort by multiple field
		//Sort sort=Sort.by("name").descending().and(Sort.by("_id").ascending());  // Another way
		return studentRepository.findAll(sort);
	}
	
	public List<Student> getStudentsByDepartmentname(String departmentname) {
		return studentRepository.findByDepartmentDepartmentName(departmentname);
	}
	
	public List<Student> getStudentsBySubjectname(String subjectname) {
		return studentRepository.findBySubjectsSubjectName(subjectname);
	}
	
	public List<Student> getStudentsByEmailLike(String email) {
		return studentRepository.findByEmailIsLike(email);
	}
	
	public List<Student> getStudentsByNameStartsWith(String name) {
		return studentRepository.findByNameStartsWith(name);
	}
	
	public List<Student> getStudentsByNameNativeQuery(String name) {
		return studentRepository.findByNameNative(name);
	}

}
