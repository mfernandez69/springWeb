package com.example.repository;

import com.example.models.Administrador;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdministradorRepository extends MongoRepository<Administrador, String> {
    Administrador findByUsername(String username);
}