import com.aws.peach.domain.order.entity.Order
import com.aws.peach.domain.order.exception.OrderStateException
import com.aws.peach.domain.order.vo.*
import com.google.common.collect.Lists
import spock.lang.Specification

class OrderTest extends Specification {

    def "calculate shipping charge"() {
        given:
        Order order = Order.builder()
                .orderLines(
                        Lists.asList(
                                OrderLine.builder()
                                        .orderProduct(
                                                OrderProduct.builder()
                                                        .productId("GOOD-PEACH-1")
                                                        .productName("좋은 복숭아")
                                                        .price(30_000)
                                                        .build())
                                        .quantity(4)
                                        .build(),
                                OrderLine.builder()
                                        .orderProduct(
                                                OrderProduct.builder()
                                                        .productId("BAD-PEACH-1")
                                                        .productName("나쁜 복숭아")
                                                        .price(10_000)
                                                        .build())
                                        .quantity(5)
                                        .build()
                        )
                )
                .orderState(OrderState.UNPAID)
                .build()
        when:
        Order.OrderLinesSummary summary = order.getOrderLinesSummary()
        then:
        summary.getShippingCharge() == 20_000
        summary.getTotalPrice() == 30_000 * 4 + 10_000 * 5 + summary.getShippingCharge()
    }

    def "change order state to SHIPPED happy case"() {
        given:
        Order order = Order.builder()
                .orderState(OrderState.PACKAGING)
                .build()
        when:
        order.ship()
        then:
        order.getOrderState() == OrderState.SHIPPED
    }

    def "if current order status is not PACKAGING, changing order state to SHIPPED should be failed"() {
        given:
        Order order

        when:
        order = Order.builder().orderState(OrderState.UNPAID).build()
        order.ship()

        then:
        thrown(OrderStateException)

        when:
        order = Order.builder().orderState(OrderState.PAID).build()
        order.ship()

        then:
        thrown(OrderStateException)

        when:
        order = Order.builder().orderState(OrderState.PREPARING).build()
        order.ship()

        then:
        thrown(OrderStateException)

        when:
        order = Order.builder().orderState(OrderState.SHIPPED).build()
        order.ship()

        then:
        thrown(OrderStateException)

        when:
        order = Order.builder().orderState(OrderState.CLOSED).build()
        order.ship()

        then:
        thrown(OrderStateException)

        when:
        order = Order.builder().orderState(OrderState.CANCELED).build()
        order.ship()

        then:
        thrown(OrderStateException)
    }
}