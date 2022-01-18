package com.aws.peach.application.order

import com.aws.peach.domain.delivery.DeliveryChangeEvent
import com.aws.peach.domain.order.entity.Order
import com.aws.peach.domain.order.repository.OrderRepository
import com.aws.peach.domain.order.vo.OrderNumber
import com.aws.peach.domain.order.vo.OrderState
import spock.lang.Specification

class OrderStateChangeServiceTest extends Specification {

    String orderId = "ORDER-1"
    OrderRepository repository
    OrderStateChangeService service

    def setup() {
        repository = Mock()
        service = new OrderStateChangeService(repository)
    }

    def "change order state in response to delivery event"(OrderState oldOrderState,
                                                           DeliveryChangeEvent.Status deliveryEventStatus,
                                                           OrderState newOrderState) {
        given:
        Optional<Order> order = createOrder(orderId, oldOrderState)
        DeliveryChangeEvent event = createDeliveryEvent(orderId, deliveryEventStatus)

        when:
        service.changeOrderState(event)

        then:
        1 * repository.findById(new OrderNumber(orderId)) >> order
        1 * repository.save({
            it instanceof Order
            it.getOrderState() == newOrderState
        })

        where:
        oldOrderState           |   deliveryEventStatus                     |   newOrderState
        OrderState.PAID         |   DeliveryChangeEvent.Status.PREPARING    |   OrderState.PREPARING
        OrderState.PREPARING    |   DeliveryChangeEvent.Status.PACKAGING    |   OrderState.PACKAGING
        OrderState.PACKAGING    |   DeliveryChangeEvent.Status.SHIPPED      |   OrderState.SHIPPED
        OrderState.SHIPPED      |   DeliveryChangeEvent.Status.DELIVERED    |   OrderState.CLOSED
    }

    static Optional<Order> createOrder(String orderId, OrderState orderState) {
        def order =  Order.builder()
                .orderNumber(OrderNumber.builder().orderNumber(orderId).build())
                .orderState(orderState)
                .build()
        return Optional.of(order)
    }

    static DeliveryChangeEvent createDeliveryEvent(String orderId, DeliveryChangeEvent.Status deliveryStatus) {
        return DeliveryChangeEvent.builder()
                .orderNo(orderId)
                .status(deliveryStatus.name())
                .build()
    }
}
