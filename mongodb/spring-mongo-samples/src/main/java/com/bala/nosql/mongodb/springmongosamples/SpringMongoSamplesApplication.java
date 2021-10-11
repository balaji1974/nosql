package com.bala.nosql.mongodb.springmongosamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.bala.nosql.mongodb.springmongosamples.repository")
@ComponentScan("com.bala.nosql.mongodb.springmongosamples.*")
public class SpringMongoSamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMongoSamplesApplication.class, args);
	}

}
