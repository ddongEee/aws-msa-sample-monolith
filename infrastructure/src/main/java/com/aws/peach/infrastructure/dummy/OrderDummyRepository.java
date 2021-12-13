package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.*;
import com.aws.peach.domain.product.entity.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDummyRepository implements OrderRepository {

    @Override
    public OrderNo nextOrderNo() {
        return new OrderNo("1");
    }

    @Override
    public Order save(Order order) {
        return order;
    }

    @Override
    public Order findById(String orderId) {
        // 04. 제품 주문 정보를 생성한다.
        List<OrderLine> orderLines = new ArrayList<OrderLine>();

        orderLines.add(OrderLine.builder().orderProduct(
                OrderProduct.builder()
                        .productName("복숭아")
                        .productId("1").build()
                )
                .quantity(10)
                .build());

        // 05. 주문을 생성한다.
        final Order order = Order.builder()
                .orderNo(OrderNo.builder().number("1").build())
                .orderer(Orderer.builder().memberId("PeachMan").name("Lee Heejong").build())
                .orderLines(orderLines)
                .shippingInformation(ShippingInformation.builder()
                        .city("Seoul")
                        .country("South Korea")
                        .receiver("Kim HakSung")
                        .telephoneNumber("010-1234-1234")
                        .zipCode("12345").build())
                .orderState(OrderState.PAY_COMPLETE)
                .build();
        return order;
    }
}
