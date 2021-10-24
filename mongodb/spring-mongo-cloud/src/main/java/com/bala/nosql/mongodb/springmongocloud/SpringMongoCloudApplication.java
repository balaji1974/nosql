package com.bala.nosql.mongodb.springmongocloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableMongoRepositories("com.bala.nosql.mongodb.springmongocloud.repository")
@ComponentScan("com.bala.nosql.mongodb.springmongocloud.*")
@SpringBootApplication
public class SpringMongoCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMongoCloudApplication.class, args);
	}

}
