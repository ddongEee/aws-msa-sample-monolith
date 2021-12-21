package com.aws.peach.domain.order.entity;

import com.aws.peach.domain.order.vo.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class Order {
    private OrderNo orderNo;
    private Orderer orderer;
    private List<OrderLine> orderLines;
    private OrderState orderState;
    private LocalDate orderDate;
    private ShippingInformation shippingInformation;

    public String getOrderNo(){
        return this.orderNo.getNumber();
    }

    public void payComplete() {
        this.orderState = OrderState.PAID;
    }

    public String getOrdererId() {
        return this.orderer.getMemberId();
    }

    public String getOrdererName() {
        return this.orderer.getName();
    }

    public boolean isEqualDate(final LocalDate date) {
        return date.isEqual(this.orderDate);
    }

    public boolean isTodayOrder() {
        final LocalDate today = LocalDate.now();
        return today.isEqual(this.orderDate);
    }

    public boolean isUnPaid() {
        return this.orderState == OrderState.UNPAID;
    }

    public OrderLinesSummary getOrderLinesSummary() {
        return new OrderLinesSummary(this.orderLines);
    }




    public static final class OrderLinesSummary {
        @Getter private final String orderedProductNameAndQuantities;
        @Getter private final int totalPrice;

        private OrderLinesSummary(final List<OrderLine> orderLines) {
            final StringBuilder orderedProductNameAndQuantities = new StringBuilder();
            final AtomicInteger totalPrice = new AtomicInteger(0);
            orderLines.forEach(orderLine -> {
                orderedProductNameAndQuantities.append(orderLine.getProductNameAndQuantity());
                totalPrice.addAndGet(orderLine.calculateAmounts());
            });
            this.orderedProductNameAndQuantities = orderedProductNameAndQuantities.toString();
            this.totalPrice = totalPrice.get();
        }
    }
}
