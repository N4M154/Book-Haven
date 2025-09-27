package com.bookstore.review.controller;

import com.bookstore.review.model.Review;
import com.bookstore.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewRepository repo;
    private final RestTemplate restTemplate;

    @Value("${book.service.url:http://localhost:8083}")
    private String bookServiceUrl;

    public ReviewController(ReviewRepository repo, RestTemplate restTemplate) { this.repo = repo; this.restTemplate = restTemplate; }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Review r) {
        try {
            String url = String.format("%s/api/books/%d", bookServiceUrl, r.getBookId());
            restTemplate.getForEntity(url, String.class);
        } catch (RestClientException ex) {
            System.out.println("Info: book-service not reachable at " + bookServiceUrl + " - skipping strict validation.");
        }
        return ResponseEntity.ok(repo.save(r));
    }

    @GetMapping
    public ResponseEntity<List<Review>> all() { return ResponseEntity.ok(repo.findAll()); }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> byBook(@PathVariable Long bookId) { return ResponseEntity.ok(repo.findByBookId(bookId)); }

    @GetMapping("/{id}")
    public ResponseEntity<Review> get(@PathVariable Long id) { return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
}
