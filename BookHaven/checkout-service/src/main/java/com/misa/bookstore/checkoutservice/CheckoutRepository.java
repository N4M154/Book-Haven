package com.misa.bookstore.checkoutservice;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CheckoutRepository extends MongoRepository<CheckoutEntity, String> {}
