package com.misa.bookstore.orderservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import com.misa.bookstore.orderservice.OrderRepository;
import com.misa.bookstore.orderservice.OrderEntity;
import com.misa.bookstore.orderservice.OrderEntity.OrderItem;
import com.misa.bookstore.orderservice.clients.UserClient;
import com.misa.bookstore.orderservice.clients.InventoryClient;
import com.misa.bookstore.orderservice.model.InventoryDto;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repo;
    private final UserClient userClient;
    private final InventoryClient inventoryClient;

    public OrderController(OrderRepository repo, UserClient userClient, InventoryClient inventoryClient) {
        this.repo = repo;
        this.userClient = userClient;
        this.inventoryClient = inventoryClient;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderEntity order) {
        // 1) Validate user exists
        try {
            userClient.getUser(order.getUserId());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error","invalid user: " + e.getMessage()));
        }

        // 2) Check inventory for each item
        if (order.getItems() != null) {
            for (OrderItem it : order.getItems()) {
                InventoryDto inv = inventoryClient.getInventory(it.getBookId());
                if (inv == null) {
                    return ResponseEntity.status(404).body(Map.of("error","no inventory for " + it.getBookId()));
                }
                if (inv.getAvailable() < it.getQty()) {
                    return ResponseEntity.status(409).body(Map.of("error","not enough stock for " + it.getBookId()));
                }
            }
        }

        // 3) Commit inventory first (decrement); if any commit fails -> return error
        if (order.getItems() != null) {
            for (OrderItem it : order.getItems()) {
                try {
                    inventoryClient.commit(it.getBookId(), Map.of("qty", it.getQty()));
                } catch (Exception e) {
                    return ResponseEntity.status(500).body(Map.of("error","inventory commit failed: "+e.getMessage()));
                }
            }
        }

        // 4) Save order
        OrderEntity saved = repo.save(order);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<?> allOrders() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") String id) {
        Optional<OrderEntity> opt = repo.findById(id);
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body((OrderEntity) Map.of("error", "order not found")));
    }
}
