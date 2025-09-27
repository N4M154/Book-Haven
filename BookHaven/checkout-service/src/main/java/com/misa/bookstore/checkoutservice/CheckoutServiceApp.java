package com.misa.bookstore.checkoutservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableFeignClients
public class CheckoutServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(CheckoutServiceApp.class, args);
    }
}

// DTO
class CheckoutRequest {
    public String customerEmail;
    public String shippingAddress;
    public java.util.List<Item> items;
    static class Item {
        public String bookId;
        public int qty;
    }
}


@FeignClient(name = "order-service")
interface OrderClient {
    @PostMapping("/orders")
    OrderResponse createOrder(@RequestBody Map<String, Object> order);
}

class OrderResponse {
    public String id;
    public String customerEmail;
    public String shippingAddress;
    public List<String> items;
    public Double totalAmount;
}

@RestController
@RequestMapping("/checkout")
class CheckoutController {
    private final CheckoutRepository repo;
    private final OrderClient orderClient;
    @Autowired
    public CheckoutController(CheckoutRepository repo, OrderClient orderClient) {
        this.repo = repo;
        this.orderClient = orderClient;
    }

    @PostMapping
    public ResponseEntity<?> createCheckout(@RequestBody CheckoutEntity checkout) {
        // Save checkout
        CheckoutEntity saved = repo.save(checkout);
        // Call OrderService to create order
        Map<String, Object> orderReq = Map.of(
            "customerEmail", checkout.getCustomerEmail(),
            "shippingAddress", checkout.getShippingAddress(),
            "items", checkout.getItems().stream().map(i -> i.getBookId()).toList(),
            "totalAmount", 0.0 // You may want to calculate this
        );
        OrderResponse order = orderClient.createOrder(orderReq);
        return ResponseEntity.ok(Map.of("checkout", saved, "order", order));
    }

    @GetMapping
    public List<CheckoutEntity> allCheckouts() {
        return repo.findAll();
    }
}
