package com.bala.nosql.mongodb.plainmavencrudsample;

import java.util.function.Consumer;

import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@SpringBootApplication
public class PlainMavenCrudSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlainMavenCrudSampleApplication.class, args);
		MongoClient mongoClient = new MongoClient();
		//MongoClient mongoClient = new MongoClient( "host1" , 27017 ); // Another way and not needed here as spring defaults handle it
		//MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://host1:27017")); // Another way to connect
		
		MongoDatabase database = mongoClient.getDatabase("test");
		MongoCollection<Document> collection = database.getCollection("employee");
		
		collection.find().forEach((Consumer<Document>) doc -> System.out.println(doc));
		mongoClient.close();
		
	}
	
	

}
