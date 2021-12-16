package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.inventory.entity.Inventory;
import com.aws.peach.domain.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InventoryDummyRepository implements InventoryRepository {

    @Override
    public Inventory findByProductIdAndDate(String productId, LocalDate localDate) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public Inventory save(Inventory inventory) {
        throw new RuntimeException("Not Implemented");
    }
}
