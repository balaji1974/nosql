package com.bala.nosql.mongodb.simplecrudsamples.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


/*
 * This whole configuration class is optional as
 * these are the default settings for 
 * Spring Data JPA - MongoDB - SpringBoot Starter Project
 * But if we need to change something that is not default it would be useful 
 */
@Configuration
public class MongoDBConfiguration {

	public @Bean MongoClient mongoClient() {
		return MongoClients.create("mongodb://localhost:27017");
	}

	public @Bean MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), "test");
	}
}
