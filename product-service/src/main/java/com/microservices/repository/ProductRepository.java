package com.microservices.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservices.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}