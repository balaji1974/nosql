package com.bala.nosql.elasticsearch.springdatajpasample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import com.bala.nosql.elasticsearch.springdatajpasample.bean.Product;

@Service
public class ProductSearchService {

	private static final String PRODUCT_INDEX = "productindex";

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	public String createProductIndex(Product product) {

		System.out.println("Here1");
		IndexQuery indexQuery = new IndexQueryBuilder().withId(product.getId().toString()).withObject(product).build();
		System.out.println("Here2");
		String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of(PRODUCT_INDEX));
		System.out.println("Here3");
		return documentId;
	}
}