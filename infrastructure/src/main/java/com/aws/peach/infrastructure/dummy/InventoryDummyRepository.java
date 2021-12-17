package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.inventory.entity.Inventory;
import com.aws.peach.domain.inventory.repository.InventoryRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class InventoryDummyRepository implements InventoryRepository {
    private List<Inventory> dummyInventoryList;

    public InventoryDummyRepository() {
        this.dummyInventoryList = Lists.newArrayList(
                Inventory.builder()
                        .productId("GOOD-PEACH-1")
                        .date(LocalDate.of(2021,12,13))
                        .count(12)
                        .build(),
                Inventory.builder()
                        .productId("GOOD-PEACH-1")
                        .date(LocalDate.of(2021,12,14))
                        .count(20)
                        .build(),
                Inventory.builder()
                        .productId("GOOD-PEACH-1")
                        .date(LocalDate.of(2021,12,15))
                        .count(30)
                        .build(),
                Inventory.builder()
                        .productId("GOOD-PEACH-2")
                        .date(LocalDate.of(2021,12,13))
                        .count(5)
                        .build(),
                Inventory.builder()
                        .productId("GOOD-PEACH-2")
                        .date(LocalDate.of(2021,12,14))
                        .count(8)
                        .build(),
                Inventory.builder()
                        .productId("GOOD-PEACH-2")
                        .date(LocalDate.of(2021,12,15))
                        .count(9)
                        .build()
        );
    }

    @Override
    public Inventory findByProductIdAndDate(String productId, LocalDate date) {
        return dummyInventoryList.stream()
                .filter(inventory -> productId.equalsIgnoreCase(inventory.getProductId()) &&
                                     date.equals(inventory.getDate()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Inventory save(Inventory inventory) {
        dummyInventoryList.add(inventory);
        return inventory;
    }
}
