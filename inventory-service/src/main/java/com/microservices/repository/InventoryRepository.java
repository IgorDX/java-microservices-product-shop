package com.microservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, int quantity);
}