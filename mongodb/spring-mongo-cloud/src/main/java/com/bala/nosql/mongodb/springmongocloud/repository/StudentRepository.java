package com.bala.nosql.mongodb.springmongocloud.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.bala.nosql.mongodb.springmongocloud.entity.Student;

@Repository
public interface StudentRepository extends MongoRepository<Student,String>{
	
	List<Student> findByName(String name);
	List<Student> findByNameAndEmail(String name, String email);
	List<Student> findByNameOrEmail(String name, String email);
	List<Student> findByDepartmentDepartmentName(String departmentname);
	List<Student> findBySubjectsSubjectName(String subjectname);
	List<Student> findByEmailIsLike(String email);
	List<Student> findByNameStartsWith(String name);
	
	@Query("{\"name\": \"?0\"}")
	List<Student> findByNameNative(String name);
}
