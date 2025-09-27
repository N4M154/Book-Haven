package com.misa.bookstore.orderservice.clients;

import com.misa.bookstore.orderservice.model.InventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/inventory/{bookId}")
    InventoryDto getInventory(@PathVariable("bookId") String bookId);

    @PostMapping("/inventory/{bookId}/commit")
    Map<String,Object> commit(@PathVariable("bookId") String bookId, @RequestBody Map<String,Integer> payload);
}
