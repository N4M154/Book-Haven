package com.misa.bookstore.cartservice.web;

import com.misa.bookstore.cartservice.model.Cart;
import com.misa.bookstore.cartservice.model.CartItem;
import com.misa.bookstore.cartservice.repo.CartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.Optional;

// Feign Clients
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
interface UserClient {
    @GetMapping("/users/{id}")
    Object getUserById(@PathVariable("id") String id);
}

@FeignClient(name = "catalog-service")
interface CatalogClient {
    @GetMapping("/books/{id}")
    Object getBookById(@PathVariable("id") String id);
}

@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartRepository repo;
    private final UserClient userClient;
    private final CatalogClient catalogClient;

    @Autowired
    public CartController(CartRepository repo, UserClient userClient, CatalogClient catalogClient) {
        this.repo = repo;
        this.userClient = userClient;
        this.catalogClient = catalogClient;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        return repo.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new Cart()));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<?> addItem(@PathVariable String userId, @RequestBody CartItem item) {
        // Validate user
        try {
            Object user = userClient.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        // Validate book
        try {
            Object book = catalogClient.getBookById(item.getBookId());
            if (book == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Book not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", "Book not found"));
        }

        // Add item
        Cart cart = repo.findByUserId(userId).orElseGet(() -> {
            Cart c = new Cart();
            c.setUserId(userId);
            return c;
        });

        boolean merged = false;
        for (CartItem it : cart.getItems()) {
            if (it.getBookId().equals(item.getBookId())) {
                it.setQty(it.getQty() + item.getQty());
                merged = true;
                break;
            }
        }
        if (!merged) cart.getItems().add(item);
        Cart saved = repo.save(cart);

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{userId}/items/{bookId}")
    public ResponseEntity<Cart> updateItem(@PathVariable String userId, @PathVariable String bookId, @RequestBody CartItem item) {
        Optional<Cart> opt = repo.findByUserId(userId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Cart cart = opt.get();
        cart.getItems().removeIf(i -> i.getBookId().equals(bookId));
        cart.getItems().add(item);
        return ResponseEntity.ok(repo.save(cart));
    }

    @DeleteMapping("/{userId}/items/{bookId}")
    public ResponseEntity<Cart> removeItem(@PathVariable String userId, @PathVariable String bookId) {
        Optional<Cart> opt = repo.findByUserId(userId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Cart cart = opt.get();
        cart.getItems().removeIf(i -> i.getBookId().equals(bookId));
        return ResponseEntity.ok(repo.save(cart));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable String userId) {
        repo.findByUserId(userId).ifPresent(c -> { repo.delete(c); });
        return ResponseEntity.ok(Map.of("status","cleared"));
    }
}
