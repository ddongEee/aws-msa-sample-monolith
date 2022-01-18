package com.aws.peach.domain.order.entity;

import com.aws.peach.domain.order.exception.OrderStateException;
import com.aws.peach.domain.order.vo.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(schema = "samplemonolith", name = "orders")
@EqualsAndHashCode(of = "orderNumber")
public class Order {
    @EmbeddedId
    private OrderNumber orderNumber;

    @Embedded
    private Orderer orderer;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "orderNumber")
    private List<OrderLine> orderLines;

    @Column(name = "orderState")
    private OrderState orderState;

    @Column(name = "orderDate")
    private LocalDate orderDate;

    @Embedded
    private ShippingInformation shippingInformation;

    public String getOrderNumber() {
        return this.orderNumber.getOrderNumber();
    }

    public void payComplete() {
        this.orderState = OrderState.PAID;
    }

    public void checkedPaid() {
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

    public void prepare() {
        if (this.orderState != OrderState.PAID) {
            throw new OrderStateException();
        }
        this.orderState = OrderState.PREPARING;
    }

    public void pack() {
        if (this.orderState != OrderState.PREPARING) {
            throw new OrderStateException();
        }
        this.orderState = OrderState.PACKAGING;
    }

    public void ship() {
        if (this.orderState != OrderState.PACKAGING) {
            throw new OrderStateException();
        }
        this.orderState = OrderState.SHIPPED;
    }

    public void close() {
        if (this.orderState != OrderState.SHIPPED) {
            throw new OrderStateException();
        }
        this.orderState = OrderState.CLOSED;
    }

    public static final class OrderLinesSummary {
        public static final int SHIPPING_CHARGE_FOR_2_BOX = 5_000;
        @Getter private final String orderedProductNameAndQuantities;
        @Getter private final int totalPrice;
        @Getter private final int shippingCharge;

        private OrderLinesSummary(final List<OrderLine> orderLines) {
            final StringBuilder orderedProductNameAndQuantities = new StringBuilder();
            final AtomicInteger totalPrice = new AtomicInteger(0);
            final AtomicInteger totalQuantity = new AtomicInteger(0);
            orderLines.forEach(orderLine -> {
                orderedProductNameAndQuantities.append(orderLine.getProductNameAndQuantity());
                totalPrice.addAndGet(orderLine.calculateAmounts());
                totalQuantity.addAndGet(orderLine.getQuantity());
            });
            this.orderedProductNameAndQuantities = orderedProductNameAndQuantities.toString();
            this.shippingCharge = calculateShippingCharge(totalQuantity.get());
            this.totalPrice = totalPrice.get() + shippingCharge;
        }

        private int calculateShippingCharge(int totalQuantity) {
            return SHIPPING_CHARGE_FOR_2_BOX * (totalQuantity / 2);
        }
    }
}
