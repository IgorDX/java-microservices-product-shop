package com.microservices.dto;

import java.math.BigDecimal;

public record OrderRequest(Long id, String skuCode, BigDecimal price, Integer quantity, UserData userData) {
    public record UserData(String email, String fistName, String lastName) {
    }
}