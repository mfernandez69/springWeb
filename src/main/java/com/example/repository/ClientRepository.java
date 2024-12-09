package com.example.repository;

import com.example.models.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ClientRepository  extends MongoRepository<Client, String> {
    @Query("{}")
    List<Client> getAllProducts();
}
