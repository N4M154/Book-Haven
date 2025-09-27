package com.misa.bookstore.inventoryservice.repo;

import com.misa.bookstore.inventoryservice.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    Optional<Inventory> findByBookId(String bookId);
}
