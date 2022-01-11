package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.vo.*;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.*;

public class OrderDummyRepository {
    private final Map<String, OrderState> orderId2OrderState;

    public OrderDummyRepository() {
        this.orderId2OrderState = new HashMap<>();
    }

//    @Override
//    public OrderNo nextOrderNo() {
//        return new OrderNo("1");
//    }

//    @Override
    public Order save(Order order) {
        this.orderId2OrderState.put(order.getOrderNumber(), order.getOrderState());
        return order;
    }

//    @Override
    public Order findById(String orderId) {
        // 04. 제품 주문 정보를 생성한다.
        List<OrderLine> orderLines = new ArrayList<>();

        orderLines.add(OrderLine.builder().orderProduct(
                OrderProduct.builder()
                        .productName("복숭아")
                        .productId("1")
                        .price(30_000)
                        .build()
                )
                .quantity(10)
                .build());

        // 05. 주문을 생성한다.
        return Order.builder()
                .orderNumber(OrderNumber.builder().orderNumber("1").build())
                .orderer(Orderer.builder().memberId("PeachMan").name("Lee Heejong").build())
                .orderLines(orderLines)
                .orderState(this.orderId2OrderState.get(orderId))
                .orderDate(LocalDate.now())
                .shippingInformation(ShippingInformation.builder()
                        .city("서울")
                        .telephoneNumber("01012341234")
                        .receiver("정우영")
                        .address1("송파구 문정동 70-6")
                        .address2("202호")
                        .build())
                .build();
    }

//    @Override
//    public List<Order> findByOrderDate(LocalDate targetDate) {
//        return this.dummyOrders.stream()
//                .filter(o -> o.isEqualDate(targetDate))
//                .filter(Order::isUnPaid)
//                .collect(Collectors.toList());
//    }

//    @Override
//    public List<Order> findByOrderNumberIn(List<String> orderNumbers) {
//        return dummyOrders.stream()
//                .filter(o -> orderNumbers.contains(o.getOrderNo()))
//                .collect(Collectors.toList());
//    }

//    @Override
//    public void saveAll(List<Order> paidUpdatedOrders) {
//        paidUpdatedOrders.forEach(paidOrder -> {
//            this.orderId2OrderState.put(paidOrder.getOrderNo(), OrderState.PAID);
//        });
//    }

    @AllArgsConstructor
    public enum DummyShippingInformation {
        HAKSUNG_SHIPPING_INFORMATION("Seoul", "010-1234-1234", "Kim HakSung", "Songpa", "101"),
        EUNJU_SHIPPING_INFORMATION("Seoul", "010-1234-1234", "Rho Eunju", "Songpa", "101");

        private final String city;
        private final String telephoneNumber;
        private final String receiver;
        private final String address1;
        private final String address2;


        public ShippingInformation make() {
            return ShippingInformation.builder()
                    .city(this.city)
                    .receiver(this.receiver)
                    .telephoneNumber(this.telephoneNumber)
                    .address1(this.address1)
                    .address2(this.address2)
                    .build();
        }
    }

    public enum ProductType {
        PEACH("1", "복숭아", 2000),
        GOLD_PEACH("2", "황금복숭아", 3000);

        private final String productId;
        private final String productName;
        private final int price;

        ProductType(final String productId, final String productName, final int price) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
        }

        private OrderProduct makeOrderProduct() {
            return OrderProduct.builder()
                    .productName(this.productName)
                    .productId(this.productId)
                    .price(this.price)
                    .build();
        }

        public OrderLine makeOrderProduct(final int quantity) {
            return OrderLine.builder()
                    .orderProduct(this.makeOrderProduct())
                    .quantity(quantity)
                    .build();
        }
    }
}
