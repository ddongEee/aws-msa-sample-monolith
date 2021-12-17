package com.aws.peach.application.inventory

import com.aws.peach.domain.inventory.entity.Inventory
import com.aws.peach.domain.inventory.repository.InventoryRepository
import spock.lang.Specification

import java.time.LocalDate

class InventoryServiceTest extends Specification {
    def "find today's inventory count happy case"() {
        given:
        LocalDate inventoryDate = LocalDate.of(2021, 12, 13)
        String productId = "GOOD-PEACH-1"
        int inventoryCount = 30
        Inventory inventory = Inventory.builder()
                .productId(productId)
                .count(inventoryCount)
                .date(inventoryDate)
                .build()

        InventoryRepository repository = Stub()
        repository.findByProductIdAndDate(productId, inventoryDate) >> inventory
        InventoryService service = new InventoryService(repository)

        when:
        int todayInventoryCount = service.getCountFor(productId, inventoryDate)

        then:
        todayInventoryCount == inventoryCount
    }

    def "today's inventory count is 0 when inventory is not exists"() {
        given:
        LocalDate inventoryDate = LocalDate.of(2021, 12, 13)
        String productId = "GOOD-PEACH-1"

        InventoryRepository repository = Stub()
        repository.findByProductIdAndDate(productId, inventoryDate) >> null
        InventoryService service = new InventoryService(repository)

        when:
        int todayInventoryCount = service.getCountFor(productId, inventoryDate)

        then:
        todayInventoryCount == 0
    }

    def "if some argument is null, throw NullPointerException when finding inventory"() {
        given:
        InventoryRepository repository = Mock()
        InventoryService service = new InventoryService(repository)

        when:
        service.getCountFor(null, LocalDate.of(2021, 12, 13))

        then:
        thrown(NullPointerException)

        when:
        service.getCountFor("GOOD-PEACH-1", null)

        then:
        thrown(NullPointerException)
    }

    def "save today's inventory count happy case"() {
        given:
        LocalDate inventoryDate = LocalDate.of(2021, 12, 13)
        String productId = "GOOD-PEACH-1"
        int inventoryCount = 30
        Inventory inventory = Inventory.builder()
                .productId(productId)
                .count(inventoryCount)
                .date(inventoryDate)
                .build()

        InventoryRepository repository = Stub()
        repository.save(inventory) >> inventory
        InventoryService service = new InventoryService(repository)

        when:
        Inventory savedInventory = service.setInventoryCountFor(productId, inventoryDate, inventoryCount)

        then:
        savedInventory == inventory
    }

    def "if some argument is null, throw NullPointerException when saving inventory"() {
        given:
        LocalDate inventoryDate = LocalDate.of(2021, 12, 13)
        String productId = "GOOD-PEACH-1"
        int inventoryCount = 30
        InventoryRepository repository = Mock()
        InventoryService service = new InventoryService(repository)

        when:
        service.setInventoryCountFor(null, inventoryDate, inventoryCount)

        then:
        thrown(NullPointerException)

        when:
        service.setInventoryCountFor(productId, null, inventoryCount)

        then:
        thrown(NullPointerException)
    }

    def "if inentoryCount is lower then 0, throw IllegalArgumentException when saving inventory"() {
        given:
        LocalDate inventoryDate = LocalDate.of(2021, 12, 13)
        String productId = "GOOD-PEACH-1"
        int inventoryCount = -5
        InventoryRepository repository = Mock()
        InventoryService service = new InventoryService(repository)

        when:
        service.setInventoryCountFor(productId, inventoryDate, inventoryCount)

        then:
        thrown(IllegalArgumentException)

    }
}
