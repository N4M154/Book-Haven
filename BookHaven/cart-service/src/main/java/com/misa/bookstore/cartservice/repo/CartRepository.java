package com.misa.bookstore.cartservice.repo;
import com.misa.bookstore.cartservice.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
}
