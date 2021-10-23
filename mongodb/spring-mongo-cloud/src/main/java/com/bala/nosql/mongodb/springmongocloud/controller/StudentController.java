package com.bala.nosql.mongodb.springmongocloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bala.nosql.mongodb.springmongocloud.entity.Student;
import com.bala.nosql.mongodb.springmongocloud.service.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@PostMapping("/create")
	public Student createStudent(@RequestBody Student student) {
		return studentService.createStudent(student);	
	}
	
	@GetMapping("/find/{id}")
	public Student getStudentById(@PathVariable String id) {
		return studentService.getStudentById(id);
	}
	
	@GetMapping("/findall")
	public List<Student> getStudents() {
		return studentService.getStudents();
	}
	
	@PutMapping("/update")
	public Student updateStudent(@RequestBody Student student) {
		return studentService.updateStudent(student);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteStudent(@PathVariable String id) {
		studentService.deleteStudentById(id);
	}
	
	@GetMapping("/findbyname/{name}")
	public List<Student> getStudentsByName(@PathVariable String name) {
		return studentService.getStudentsByName(name);
	}
	
	@GetMapping("/findbynameandemail")
	public List<Student> getStudentsByNameAndEmail(@RequestParam String name,@RequestParam String email) {
		return studentService.getStudentsByNameAndEmail(name,email);
	}
	
	@GetMapping("/findbynameoremail")
	public List<Student> getStudentsByNameOrEmail(@RequestParam String name,@RequestParam String email) {
		return studentService.getStudentsByNameOrEmail(name,email);
	}
	
	@GetMapping("/findallwithpagination")
	public List<Student> getStudentsWithPagination(@RequestParam int pageno,@RequestParam int pagesize) {
		return studentService.getStudentsWithPagination(pageno,pagesize);
	}
	
	@GetMapping("/findallsortbyname")
	public List<Student> getStudentsSortByName() {
		return studentService.getStudentsSortByName();
	}
	
	@GetMapping("/findbydepartmentname/{departmentname}")
	public List<Student> getStudentsByDepartnameName(@PathVariable String departmentname) {
		return studentService.getStudentsByDepartmentname(departmentname);
	}
	
	@GetMapping("/findbysubjectname/{subjectname}")
	public List<Student> getStudentsBySubjectName(@PathVariable String subjectname) {
		return studentService.getStudentsBySubjectname(subjectname);
	}
	
	@GetMapping("/findbyemaillike/{email}")
	public List<Student> getStudentsByEmailLike(@PathVariable String email) {
		return studentService.getStudentsByEmailLike(email);
	}
	
	@GetMapping("/findbynamestartswith/{name}")
	public List<Student> getStudentsByNameStartsWith(@PathVariable String name) {
		return studentService.getStudentsByNameStartsWith(name);
	}
	
	@GetMapping("/findbynamenativequery/{name}")
	public List<Student> getStudentsByNameNativeQuery(@PathVariable String name) {
		return studentService.getStudentsByNameNativeQuery(name);
	}
}
