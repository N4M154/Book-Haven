package com.misa.bookstore.inventoryservice.web;

import com.misa.bookstore.inventoryservice.model.Inventory;
import com.misa.bookstore.inventoryservice.repo.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryRepository repo;
    public InventoryController(InventoryRepository repo) { this.repo = repo; }

    // Get inventory info
    @GetMapping("/{bookId}")
    public ResponseEntity<?> get(@PathVariable String bookId) {
        return repo.findByBookId(bookId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // commit: reduce available by qty (purchase)
    @PostMapping("/{bookId}/commit")
    public ResponseEntity<?> commit(@PathVariable String bookId, @RequestBody Map<String,Integer> payload) {
        int qty = payload.getOrDefault("qty", 1);
        Inventory inv = repo.findByBookId(bookId).orElse(null);
        if (inv == null) return ResponseEntity.status(404).body(Map.of("error","not found"));
        if (inv.getAvailable() < qty) return ResponseEntity.status(409).body(Map.of("error","not enough stock"));
        inv.setAvailable(inv.getAvailable() - qty);
        repo.save(inv);
        return ResponseEntity.ok(Map.of("available", inv.getAvailable()));
    }

    // optional admin endpoint to set stock
    @PostMapping("/{bookId}/set")
    public ResponseEntity<?> set(@PathVariable String bookId, @RequestBody Map<String,Integer> payload) {
        int qty = payload.getOrDefault("qty", 0);
        Inventory inv = repo.findByBookId(bookId).orElseGet(() -> { Inventory i = new Inventory(); i.setBookId(bookId); i.setAvailable(0); i.setReserved(0); return i; });
        inv.setAvailable(qty);
        repo.save(inv);
        return ResponseEntity.ok(inv);
    }
}
