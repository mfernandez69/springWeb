package com.example.repository;

import com.example.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order,String> {

    @Query("{}")
    List<Order> getAllOrders();
}
