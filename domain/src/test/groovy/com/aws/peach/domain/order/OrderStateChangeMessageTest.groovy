package com.aws.peach.domain.order

import com.aws.peach.domain.order.entity.Order
import com.aws.peach.domain.order.vo.OrderLine
import com.aws.peach.domain.order.vo.OrderNumber
import com.aws.peach.domain.order.vo.OrderProduct
import com.aws.peach.domain.order.vo.OrderState
import com.aws.peach.domain.order.vo.Orderer
import com.aws.peach.domain.order.vo.ShippingInformation
import spock.lang.Specification

import java.time.LocalDate

class OrderStateChangeMessageTest extends Specification {
    def "should create OrderStateChangeMessage from Order"() {
        given:
        Order order = createOrder()

        when:
        OrderStateChangeMessage msg = OrderStateChangeMessage.paidCompleted(order)

        then:
        msg.getOrderNumber() == order.getOrderNumber()
        msg.getOrdererId() == order.getOrdererId()
        msg.getOrdererName() == order.getOrdererName()
        msg.getOrderLines().size() == order.getOrderLines().size()
        msg.getStatus() == OrderState.PAID.name()
        msg.getOrderDate() != null
        msg.getShippingInformation().getReceiver() == order.getShippingInformation().getReceiver()
    }

    Order createOrder() {
        Orderer orderer = Orderer.builder()
                .memberId("PeachMan")
                .name("Benjamin")
                .build()
        OrderProduct whitePeach = OrderProduct.builder()
                .productId("WP")
                .productName("White Peach")
                .build()
        OrderProduct yellowPeach = OrderProduct.builder()
                .productId("YP")
                .productName("Yellow Peach")
                .build()
        List<OrderLine> orderLines = Arrays.asList(
                OrderLine.builder().orderProduct(whitePeach).quantity(1).build(),
                OrderLine.builder().orderProduct(yellowPeach).quantity(2).build()
        )
        ShippingInformation shipping = ShippingInformation.builder()
                .receiver("Alice")
                .telephoneNumber("010-1234-5678")
                .city("Seoul")
                .address1("Teheran-ro 100")
                .address2("Royal Garden")
                .build()

        return Order.builder()
                .orderNumber(new OrderNumber("123"))
                .orderer(orderer)
                .orderLines(orderLines)
                .orderState(OrderState.UNPAID)
                .orderDate(LocalDate.now())
                .shippingInformation(shipping)
                .build()
    }
}
