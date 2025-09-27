package com.misa.bookstore.userservice.web;

import com.misa.bookstore.userservice.model.UserEntity;
import com.misa.bookstore.userservice.repo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;
    public UserController(UserRepository repo) { this.repo = repo; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        String name = payload.get("name");
        String phone = payload.get("phone");
        if (email == null || password == null) return ResponseEntity.badRequest().body("email & password required");
        if (repo.findByEmail(email).isPresent()) return ResponseEntity.status(409).body("email exists");
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        UserEntity u = new UserEntity();
        u.setEmail(email); u.setPasswordHash(hashed); u.setName(name); u.setPhone(phone);
        UserEntity saved = repo.save(u);
        saved.setPasswordHash(null);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        Optional<UserEntity> opt = repo.findByEmail(email);
        if (opt.isEmpty()) return ResponseEntity.status(401).body("invalid credentials");
        UserEntity u = opt.get();
        if (!BCrypt.checkpw(password, u.getPasswordHash())) return ResponseEntity.status(401).body("invalid credentials");
        return ResponseEntity.ok(Map.of("id", u.getId(), "email", u.getEmail()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return repo.findById(id).map(u -> {
            u.setPasswordHash(null); return ResponseEntity.ok(u);
        }).orElse(ResponseEntity.notFound().build());
    }
}
