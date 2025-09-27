package com.misa.bookstore.orderservice;

import com.misa.bookstore.orderservice.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {}
