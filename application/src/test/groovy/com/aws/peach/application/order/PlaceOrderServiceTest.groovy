package com.aws.peach.application.order

import com.aws.peach.application.inventory.InventoryService
import com.aws.peach.domain.member.MemberRepository
import com.aws.peach.domain.order.exception.OutOfOrderException
import com.aws.peach.domain.order.repository.OrderRepository
import com.aws.peach.domain.product.repository.ProductRepository
import com.google.common.collect.Lists
import spock.lang.Specification

class PlaceOrderServiceTest extends Specification {
    def "if inventory is empty, raise OutOfOrderException"() {
        given:
        InventoryService inventoryService = Stub()
        inventoryService.isOutOfStock(_ as List<InventoryService.CheckOrderProduct>) >> true
        OrderRepository orderRepository = Mock()
        MemberRepository memberRepository = Mock()
        ProductRepository productRepository = Mock()
        PlaceOrderService placeOrderService = new PlaceOrderService(
                inventoryService,
                orderRepository,
                memberRepository,
                productRepository
        )
        PlaceOrderService.ShippingRequest shippingRequest = Mock()
        PlaceOrderService.PlaceOrderRequest request = PlaceOrderService.PlaceOrderRequest.builder()
        .orderer("정우영")
        .orderLines(
                Lists.asList(
                PlaceOrderService.OrderRequestLine.of("GOOD-PEACH-1", 3)
        ))
        .shippingRequest(shippingRequest)
        .build()

        when:
        placeOrderService.placeOrder(request)

        then:
        def e = thrown(OutOfOrderException)
        e.getMessage() == "재고가 초과되어 주문에 실패하였습니다. 내일 주문 해주세요."
    }
}
