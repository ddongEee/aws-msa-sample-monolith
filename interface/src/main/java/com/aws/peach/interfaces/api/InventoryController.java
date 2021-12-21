package com.aws.peach.interfaces.api;

import com.aws.peach.application.inventory.InventoryService;
import com.aws.peach.domain.inventory.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@RestController
@RequestMapping("/inventories")
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @GetMapping("/{productId}/{date}/count")
    public ResponseEntity<Integer> getInventoryCountFor(
            @PathVariable String productId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        log.info("getInventoryCountFor({}, {})", productId, date);
        return ResponseEntity.ok(inventoryService.getCountFor(productId, date));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Inventory> saveInventoryCount(@PathVariable String productId,
                                                     @RequestBody @Valid SaveInventoryCountRequest request) {
        log.info("saveInventory({}, {})", productId, request);
        Inventory saved = inventoryService.setInventoryCountFor(productId, request.getDate(), request.getCount());
        return ResponseEntity.ok(saved);
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @ToString
    public static final class SaveInventoryCountRequest {
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        @PositiveOrZero
        private int count;
    }
}
