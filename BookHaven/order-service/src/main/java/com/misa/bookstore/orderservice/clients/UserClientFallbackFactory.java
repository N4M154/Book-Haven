package com.misa.bookstore.orderservice.clients;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public Map<String, Object> getUser(String id) {
                // Customize fallback behaviour here. For now, throw an explicit runtime error so controller handles it.
                throw new RuntimeException("user-service unavailable: " + (cause == null ? "unknown" : cause.getMessage()), cause);
            }
        };
    }
}
