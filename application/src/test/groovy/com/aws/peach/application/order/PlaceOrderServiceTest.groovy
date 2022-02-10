package com.aws.peach.application.order

import com.aws.peach.application.inventory.InventoryService
import com.aws.peach.domain.member.MemberRepository
import com.aws.peach.domain.order.entity.Order
import com.aws.peach.domain.order.exception.OutOfOrderException
import com.aws.peach.domain.order.repository.OrderRepository
import com.aws.peach.domain.order.vo.OrderNumber
import com.aws.peach.domain.order.vo.OrderState
import com.aws.peach.domain.order.vo.ShippingInformation
import com.aws.peach.domain.product.entity.Product
import com.aws.peach.domain.product.repository.ProductRepository
import com.google.common.collect.Lists
import spock.lang.Ignore
import spock.lang.Specification

@Ignore
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

    def "place order happy case"() {
        given:
        InventoryService inventoryService = Stub()
        inventoryService.isOutOfStock(_ as List<InventoryService.CheckOrderProduct>) >> false
        OrderRepository orderRepository = Spy()
        MemberRepository memberRepository = Stub()
        ProductRepository productRepository = Stub()
        productRepository.findByIdIn(_ as List<String>) >> Lists.asList(
                Product.builder()
                .id("GOOD-PEACH-1")
                .name("좋은 복숭아 1")
                .price(3000)
                .build()
        )
        PlaceOrderService placeOrderService = new PlaceOrderService(
                inventoryService,
                orderRepository,
                memberRepository,
                productRepository
        )
        PlaceOrderService.ShippingRequest shippingRequest = PlaceOrderService.ShippingRequest.builder()
                .city("서울")
                .telephoneNumber("01012341234")
                .receiver("정우영")
                .address1("송파구 문정동 70-6")
                .address2("202호")
                .build()
        PlaceOrderService.PlaceOrderRequest request = PlaceOrderService.PlaceOrderRequest.builder()
                .orderer("정우영")
                .orderLines(
                        Lists.asList(
                                PlaceOrderService.OrderRequestLine.of("GOOD-PEACH-1", 3)
                        ))
                .shippingRequest(shippingRequest)
                .build()
        // for saved argument capture
        Order savedOrder


        when:
        String orderNo = placeOrderService.placeOrder(request)

        then:
        1 * orderRepository.nextOrderNo() >> OrderNumber.builder().orderNumber("ORDER-1").build()
        1 * orderRepository.save(_ as Order) >> {arguments -> savedOrder=arguments[0]}
        orderNo == "ORDER-1"
        savedOrder instanceof Order
        savedOrder.getOrderNumber() == "ORDER-1"
        savedOrder.getOrderState() == OrderState.UNPAID
        ShippingInformation savedShippingInformation = savedOrder.getShippingInformation()
        savedShippingInformation.city == shippingRequest.city
        savedShippingInformation.receiver == shippingRequest.receiver
        savedShippingInformation.telephoneNumber == shippingRequest.telephoneNumber
        savedShippingInformation.address1 == shippingRequest.address1
        savedShippingInformation.address2 == shippingRequest.address2
    }
}
