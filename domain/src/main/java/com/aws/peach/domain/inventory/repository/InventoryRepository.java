package com.aws.peach.domain.inventory.repository;

import com.aws.peach.domain.inventory.entity.Inventory;

import java.time.LocalDate;

public interface InventoryRepository {
    Inventory findByProductIdAndDate(String productId, LocalDate localDate);
    Inventory save(Inventory inventory);
}
