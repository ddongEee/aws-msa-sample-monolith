package com.aws.peach.infrastructure.aurora;

import com.aws.peach.domain.inventory.entity.Inventory;
import com.aws.peach.domain.inventory.repository.InventoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryAuroraRepository extends InventoryRepository, JpaRepository<Inventory, Long> {
}
