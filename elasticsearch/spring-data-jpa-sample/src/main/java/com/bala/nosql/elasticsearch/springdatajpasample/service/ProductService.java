package com.bala.nosql.elasticsearch.springdatajpasample.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bala.nosql.elasticsearch.springdatajpasample.bean.Product;
import com.bala.nosql.elasticsearch.springdatajpasample.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	public void createProductIndexBulk(final List<Product> products) {
		productRepository.saveAll(products);
	}

	public void createProductIndex(final Product product) {
		productRepository.save(product);
	}
	
	public void findProductByName(String name) {
		productRepository.findByName(name).stream().forEach(s -> System.out.println(s.getName()));
	}

}
