package com.misa.bookstore.catalogservice.web;
import com.misa.bookstore.catalogservice.model.Book;
import com.misa.bookstore.catalogservice.repo.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class CatalogController {
    private final BookRepository repo;

    public CatalogController(BookRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Book> list(@RequestParam(required = false) String q) {
        if (q == null || q.isBlank()) return repo.findAll();
        return repo.findByTitleContainingIgnoreCase(q);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable String id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add this POST mapping to allow creating books
    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Book saved = repo.save(book);
        return ResponseEntity.ok(saved);
    }

    // Optional: update book
    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable String id, @RequestBody Book book) {
        return repo.findById(id).map(existing -> {
            existing.setTitle(book.getTitle());
            existing.setAuthor(book.getAuthor());
            existing.setPrice(book.getPrice());
            existing.setDescription(book.getDescription());
            existing.setCoverUrl(book.getCoverUrl());
            existing.setIsbn(book.getIsbn());
            existing.setStock(book.getStock()); // if you added stock
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Optional: delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("status","deleted"));
    }
}
