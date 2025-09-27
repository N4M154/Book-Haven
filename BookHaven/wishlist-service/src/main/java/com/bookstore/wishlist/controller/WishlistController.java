package com.bookstore.wishlist.controller;

import com.bookstore.wishlist.model.WishlistItem;
import com.bookstore.wishlist.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap; import java.util.List; import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistRepository repo;
    private final RestTemplate restTemplate;
    @Value("${user.service.url:http://localhost:8081}") private String userServiceUrl;
    @Value("${cart.service.url:http://localhost:8082}") private String cartServiceUrl;
    public WishlistController(WishlistRepository repo, RestTemplate restTemplate){ this.repo=repo; this.restTemplate=restTemplate; }

    @PostMapping public ResponseEntity<?> add(@RequestBody WishlistItem item){
        try{ String userCheckUrl = String.format(userServiceUrl + "/api/users/%d", item.getUserId()); restTemplate.getForEntity(userCheckUrl, String.class); }
        catch(RestClientException ex){ System.out.println("Warning: user-service not reachable - skipping validation."); }
        WishlistItem saved = repo.save(item);
        try{ String cartNotifyUrl = String.format(cartServiceUrl + "/api/cart/notify-wishlist/%d", item.getUserId()); Map<String,Object> payload = new HashMap<>(); payload.put("wishlistItemId", saved.getId()); payload.put("bookId", saved.getBookId()); restTemplate.postForEntity(cartNotifyUrl, payload, String.class); }
        catch(RestClientException ex){ System.out.println("Info: cart-service not reachable - notification skipped."); }
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user/{userId}") public ResponseEntity<List<WishlistItem>> byUser(@PathVariable Long userId){ return ResponseEntity.ok(repo.findByUserId(userId)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){ repo.deleteById(id); return ResponseEntity.noContent().build(); }
}
