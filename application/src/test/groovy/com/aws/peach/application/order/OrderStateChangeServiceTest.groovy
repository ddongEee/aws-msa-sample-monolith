package com.aws.peach.application.order

import com.aws.peach.domain.delivery.DeliveryChangeEvent
import com.aws.peach.domain.order.entity.Order
import com.aws.peach.domain.order.repository.OrderRepository
import com.aws.peach.domain.order.vo.OrderNo
import com.aws.peach.domain.order.vo.OrderState
import spock.lang.Specification

class OrderStateChangeServiceTest extends Specification {
    def "change state to CLOSED happy case"() {
        given:
        Order order = Order.builder()
                .orderNo(OrderNo.builder().number("ORDER-1").build())
                .orderState(OrderState.SHIPPED)
                .build()
        Order savedOrder
        OrderRepository repository = Spy()
        OrderStateChangeService service = new OrderStateChangeService(repository);
        DeliveryChangeEvent event = DeliveryChangeEvent.builder()
                .orderNo("ORDER-1")
                .status(DeliveryChangeEvent.Status.DELIVERED.name())
                .build()

        when:
        service.changeOrderState(event)

        then:
        1 * repository.findById("ORDER-1") >> order
        1 * repository.save(_ as Order) >> { argument -> savedOrder=argument[0]}
        savedOrder.getOrderState() == OrderState.CLOSED
    }
}
