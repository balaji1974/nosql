package com.bala.nosql.elasticsearch.springdatajpasample.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bala.nosql.elasticsearch.springdatajpasample.bean.Product;

// Here the ProductRepository class inherits the methods like 
// save(), saveAll(), find(), and findAll() are included from the ElasticsearchRepository interface.
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
	
	// 3 sample methods added
	List<Product> findByName(String name);

	List<Product> findByNameContaining(String name);

	List<Product> findByManufacturerAndCategory(String manufacturer, String category);

}
