package com.bala.nosql.mongodb.springmongosamples.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bala.nosql.mongodb.springmongosamples.entity.Student;

@Repository
public interface StudentRepository extends MongoRepository<Student,String>{
	
}
