import com.aws.peach.domain.order.entity.Order
import com.aws.peach.domain.order.vo.OrderLine
import com.aws.peach.domain.order.vo.OrderNo
import com.aws.peach.domain.order.vo.OrderProduct
import com.aws.peach.domain.order.vo.OrderState
import com.aws.peach.domain.order.vo.Orderer
import com.aws.peach.domain.order.vo.ShippingInformation
import com.google.common.collect.Lists
import spock.lang.Specification

import java.time.LocalDate

class OrderTest extends Specification {

    def "calculate shipping charge"() {
        given:
        Order order = Order.builder()
                .orderNo(OrderNo.builder()
                        .number("ORDER-NO-1")
                        .build()
                )
                .orderer(Orderer.builder()
                        .memberId("wooyounj")
                        .name("정우영")
                        .build())
                .orderLines(
                        Lists.asList(
                                OrderLine.builder()
                                        .orderProduct(
                                                OrderProduct.builder()
                                                        .productId("GOOD-PEACH-1")
                                                        .productName("좋은 복숭아")
                                                        .price(30_000)
                                                        .build())
                                        .quantity(3)
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
                .orderDate(LocalDate.now())
                .orderState(OrderState.UNPAID)
                .shippingInformation(
                        ShippingInformation.builder()
                                .city("서울")
                                .receiver("김학성")
                                .telephoneNumber("010-1234-1234")
                                .address1("송파구 송파동")
                                .address2("XX 아파트 505호")
                                .build()
                )
                .build()
        when:
        Order.OrderLinesSummary summary = order.getOrderLinesSummary()
        then:
        summary.getShippingCharge() == 20_000
        summary.getTotalPrice() == 30_000 * 3 + 10_000 * 5 + summary.getShippingCharge()
    }
}