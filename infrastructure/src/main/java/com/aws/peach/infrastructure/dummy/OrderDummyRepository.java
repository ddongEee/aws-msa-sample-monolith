package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        List<OrderLine> orderLines = new ArrayList<>();

        orderLines.add(OrderLine.builder().orderProduct(
                OrderProduct.builder()
                        .productName("복숭아")
                        .productId("1").build()
                )
                .quantity(10)
                .build());

        // 05. 주문을 생성한다.
        return Order.builder()
                .orderNo(OrderNo.builder().number("1").build())
                .orderer(Orderer.builder().memberId("PeachMan").name("Lee Heejong").build())
                .orderLines(orderLines)
                .orderState(OrderState.PAY_COMPLETE)
                .shippingInformation(ShippingInformation.builder()
                        .city("서울")
                        .telephoneNumber("01012341234")
                        .receiver("정우영")
                        .address1("송파구 문정동 70-6")
                        .address2("202호")
                        .build())
                .build();
    }
}
