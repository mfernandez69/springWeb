package com.example.repository;

import com.example.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{}")
    List<Product> getAllProducts();
}
