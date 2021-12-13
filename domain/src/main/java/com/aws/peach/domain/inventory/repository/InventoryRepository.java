package com.aws.peach.domain.inventory.repository;

import com.aws.peach.domain.inventory.Inventory;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.vo.OrderNo;

public interface InventoryRepository {
    public Inventory get(final String inventoryId);
}
