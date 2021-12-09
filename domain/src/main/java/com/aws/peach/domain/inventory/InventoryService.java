package com.aws.peach.domain.inventory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryService {
    public boolean isOutOfStock(List<CheckOrderProduct> orderProducts) {
        return false;
    }

    @AllArgsConstructor(staticName = "of")
    public static final class CheckOrderProduct {
        private String productId;
        private int quantity;
    }
}
