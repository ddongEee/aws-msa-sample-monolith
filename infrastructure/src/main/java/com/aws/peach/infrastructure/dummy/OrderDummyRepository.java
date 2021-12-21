package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.aws.peach.infrastructure.dummy.OrderDummyRepository.DummyShippingInformation.EUNJU_SHIPPING_INFORMATION;
import static com.aws.peach.infrastructure.dummy.OrderDummyRepository.DummyShippingInformation.HAKSUNG_SHIPPING_INFORMATION;
import static com.aws.peach.infrastructure.dummy.OrderDummyRepository.ProductType.GOLD_PEACH;
import static com.aws.peach.infrastructure.dummy.OrderDummyRepository.ProductType.PEACH;

@Component
public class OrderDummyRepository implements OrderRepository {
    private final List<Order> dummyOrders;

    public OrderDummyRepository() {
        this.dummyOrders = loadDummyOrders();
    }

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

    @Override
    public List<Order> findByOrderDate(LocalDate targetDate) {
        return this.dummyOrders.stream()
                .filter(o -> o.isEqualDate(targetDate))
                .filter(Order::isUnPaid)
                .collect(Collectors.toList());
    }

    @AllArgsConstructor
    public enum DummyShippingInformation {
        HAKSUNG_SHIPPING_INFORMATION("Seoul", "South Korea", "Kim HakSung", "010-1234-1234", "12345"),
        EUNJU_SHIPPING_INFORMATION("Seoul", "South Korea", "Rho Eunju", "010-1234-1234", "78910");

        private final String country;
        private final String city;
        private final String zipCode;
        private final String telephoneNumber;
        private final String receiver;

        public ShippingInformation make() {
            return ShippingInformation.builder()
                    .city(this.city)
                    .country(this.country)
                    .receiver(this.receiver)
                    .telephoneNumber(this.telephoneNumber)
                    .zipCode(this.zipCode)
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

    private List<Order> loadDummyOrders() {
        return Arrays.asList(Order.builder()
                        .orderNo(OrderNo.builder().number("1").build())
                        .orderer(Orderer.builder().memberId("PeachMan").name("Lee Heejong").build())
                        .orderLines(Collections.singletonList(PEACH.makeOrderProduct(10)))
                        .shippingInformation(HAKSUNG_SHIPPING_INFORMATION.make())
                        .orderState(OrderState.UNPAID) // todo : crayon : 어제 주문이지만 아직 결제가 안된 케이스 커버는?
                        .orderDate(LocalDate.now().minusDays(1)) // 어제
                        .build(),
                Order.builder()
                        .orderNo(OrderNo.builder().number("2").build())
                        .orderer(Orderer.builder().memberId("PeachMan").name("Lee Heejong").build())
                        .orderLines(Collections.singletonList(PEACH.makeOrderProduct(20)))
                        .shippingInformation(HAKSUNG_SHIPPING_INFORMATION.make())
                        .orderState(OrderState.PAID)
                        .orderDate(LocalDate.now()) // 오늘
                        .build(),
                Order.builder()
                        .orderNo(OrderNo.builder().number("3").build())
                        .orderer(Orderer.builder().memberId("PeachMan").name("Lee Heejong").build())
                        .orderLines(Arrays.asList(PEACH.makeOrderProduct(20), GOLD_PEACH.makeOrderProduct(10)))
                        .shippingInformation(EUNJU_SHIPPING_INFORMATION.make())
                        .orderState(OrderState.UNPAID)
                        .orderDate(LocalDate.now()) // 오늘
                        .build(),
                Order.builder()
                        .orderNo(OrderNo.builder().number("4").build())
                        .orderer(Orderer.builder().memberId("HealthMan").name("Jung Wooyoung").build())
                        .orderLines(Arrays.asList(PEACH.makeOrderProduct(10), GOLD_PEACH.makeOrderProduct(10)))
                        .shippingInformation(EUNJU_SHIPPING_INFORMATION.make())
                        .orderState(OrderState.UNPAID)
                        .orderDate(LocalDate.now()) // 오늘
                        .build(),
                Order.builder()
                        .orderNo(OrderNo.builder().number("5").build())
                        .orderer(Orderer.builder().memberId("CookieMan").name("Kim Haksung").build())
                        .orderLines(Arrays.asList(PEACH.makeOrderProduct(5), GOLD_PEACH.makeOrderProduct(5)))
                        .shippingInformation(EUNJU_SHIPPING_INFORMATION.make())
                        .orderState(OrderState.UNPAID) // 결제됨
                        .orderDate(LocalDate.now()) // 오늘
                        .build()
        );
    }
}
