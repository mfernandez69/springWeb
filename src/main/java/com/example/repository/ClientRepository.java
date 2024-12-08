package com.example.repository;

import com.example.models.Client;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository  extends MongoRepository<Client, String> {
}
