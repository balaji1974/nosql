package com.bala.nosql.redis.springbootsample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootSampleApplication implements CommandLineRunner{
	
	@Autowired
	RedisOperations redisOperations;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSampleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		redisOperations.saveStudents();
		redisOperations.findAllStudents();
		
		
	}
	
	

}
