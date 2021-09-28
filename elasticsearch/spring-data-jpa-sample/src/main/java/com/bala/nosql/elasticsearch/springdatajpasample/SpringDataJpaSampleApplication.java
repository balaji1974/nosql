package com.bala.nosql.elasticsearch.springdatajpasample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.bala.nosql.elasticsearch.springdatajpasample.bean.Product;
import com.bala.nosql.elasticsearch.springdatajpasample.service.ProductSearchService;
import com.bala.nosql.elasticsearch.springdatajpasample.service.ProductService;

@SpringBootApplication
@Component
public class SpringDataJpaSampleApplication implements  CommandLineRunner{
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductSearchService productSearchService; // Another way of searching

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaSampleApplication.class, args);
	}
	
	

	@Override
	public void run(String... args) throws Exception {
		
		Product product = new Product("FountainPen", 75.35, 15, "Stationary", "This is a ball point pen",
				"Reynolds");
		
		
		productService.createProductIndex(product);
		productService.findProductByName("FountainPen");
		
		//productSearchService.createProductIndex(product);
	}

}
