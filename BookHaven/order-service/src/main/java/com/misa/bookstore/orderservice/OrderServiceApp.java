package com.misa.bookstore.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
// <-- make sure the basePackages points to the package where your Feign client interfaces live
@EnableFeignClients(basePackages = "com.misa.bookstore.orderservice.clients")
public class OrderServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApp.class, args);
    }
}
