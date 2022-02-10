package com.aws.peach.application.inventory;

import com.aws.peach.domain.inventory.entity.Inventory;
import com.aws.peach.domain.inventory.repository.InventoryRepository;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional(readOnly = true, transactionManager = "transactionManager")
public class InventoryService {
    private final InventoryRepository repository;

    public InventoryService(final InventoryRepository repository) {
        this.repository = repository;
    }

    public boolean isOutOfStock(List<CheckOrderProduct> orderProducts) {
        return orderProducts.stream().anyMatch(this::isOutOfStockTodayFor);
    }

    private boolean isOutOfStockTodayFor(CheckOrderProduct checkOrderProduct) {
        Inventory inventoryForToday = repository.findByProductIdAndDate(checkOrderProduct.productId, LocalDate.now());
        return inventoryForToday != null && inventoryForToday.getCount() < checkOrderProduct.quantity;
    }

    public int getCountFor(final String productId, final LocalDate date) {
        Preconditions.checkNotNull(productId);
        Preconditions.checkNotNull(date);
        Inventory inventory = repository.findByProductIdAndDate(productId, date);

        // 해당 날짜의 재고가 입력되지 않았다.
        if(inventory == null) {
            return 0;
        }
        return inventory.getCount();
    }

    @Transactional(transactionManager = "transactionManager")
    public Inventory setInventoryCountFor(String productId, LocalDate date, int newInventoryCount) {
        Preconditions.checkNotNull(productId);
        Preconditions.checkNotNull(date);
        if(newInventoryCount < 0) {
            throw new IllegalArgumentException("newInventoryCount is lower then 0");
        }
        Inventory newInventory = Inventory.builder()
                .productId(productId)
                .date(date)
                .count(newInventoryCount)
                .build();
        return repository.save(newInventory);
    }

    @AllArgsConstructor(staticName = "of")
    public static final class CheckOrderProduct {
        private String productId;
        private int quantity;
    }

}
