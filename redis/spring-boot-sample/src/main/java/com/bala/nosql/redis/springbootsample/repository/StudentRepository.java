package com.bala.nosql.redis.springbootsample.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bala.nosql.redis.springbootsample.entity.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, String> {}