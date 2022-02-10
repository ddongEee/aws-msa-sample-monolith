package com.aws.peach.application.order

import com.aws.peach.domain.delivery.DeliveryChangeMessage
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

    def "change order state in response to delivery message"(OrderState oldOrderState,
                                                           DeliveryChangeMessage.Status deliveryStatus,
                                                           OrderState newOrderState) {
        given:
        Optional<Order> order = createOrder(orderId, oldOrderState)
        DeliveryChangeMessage message = createDeliveryMessage(orderId, deliveryStatus)

        when:
        service.changeOrderState(message)

        then:
        1 * repository.findById(new OrderNumber(orderId)) >> order
        1 * repository.save({
            it instanceof Order
            it.getOrderState() == newOrderState
        })

        where:
        oldOrderState           |   deliveryStatus                         |   newOrderState
        OrderState.PAID         |   DeliveryChangeMessage.Status.PREPARING |   OrderState.PREPARING
        OrderState.PREPARING    |   DeliveryChangeMessage.Status.PACKAGING |   OrderState.PACKAGING
        OrderState.PACKAGING    |   DeliveryChangeMessage.Status.SHIPPED   |   OrderState.SHIPPED
        OrderState.SHIPPED      |   DeliveryChangeMessage.Status.DELIVERED |   OrderState.CLOSED
    }

    static Optional<Order> createOrder(String orderId, OrderState orderState) {
        def order =  Order.builder()
                .orderNumber(OrderNumber.builder().orderNumber(orderId).build())
                .orderState(orderState)
                .build()
        return Optional.of(order)
    }

    static DeliveryChangeMessage createDeliveryMessage(String orderId, DeliveryChangeMessage.Status deliveryStatus) {
        return DeliveryChangeMessage.builder()
                .orderNo(orderId)
                .status(deliveryStatus.name())
                .build()
    }
}
