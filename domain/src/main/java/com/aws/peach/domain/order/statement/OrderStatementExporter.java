package com.aws.peach.domain.order.statement;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.*;

@Component
public class OrderStatementExporter {
    private final OrderRepository orderRepository;

    public OrderStatementExporter(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // http://blog.tremblay.pro/2019/08/map.html
    public List<GroupedOrderStatement> loadUnpaidGroupedOrderStatementsByDate(final LocalDate targetDate) {
        List<Order> orders = orderRepository.findByOrderDate(targetDate);
        final Map<String, GroupedOrderStatement> groupedOrderStatementByOrdererIdMap = new HashMap<>();

        for (Order order : orders) {
            groupedOrderStatementByOrdererIdMap.compute(order.getOrdererId(), (ordererId, groupedOrderStatement) -> {
                if (groupedOrderStatement == null) {
                    return GroupedOrderStatement.create(order);
                }
                return groupedOrderStatement.update(order);
            });
        }

        return new ArrayList<>(groupedOrderStatementByOrdererIdMap.values());
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GroupedOrderStatement {
        @Getter private final String groupedOrdererMemberId;
        @Getter private final String groupedOrdererName;
        @Getter private final int totalPayablePrice;
        @Getter private final List<OrderStatement> orderStatements;

        public static GroupedOrderStatement create(final Order order) {
            Order.OrderLinesSummary orderLinesSummary = order.getOrderLinesSummary();
            OrderStatement orderStatement = OrderStatement.builder()
                    .orderNumber(order.getOrderNumber())
                    .orderedProductNameAndQuantities(orderLinesSummary.getOrderedProductNameAndQuantities())
                    .calculatedPrice(orderLinesSummary.getTotalPrice())
                    .build();
            return builder()
                    .groupedOrdererMemberId(order.getOrdererId())
                    .groupedOrdererName(order.getOrdererName())
                    .totalPayablePrice(orderLinesSummary.getTotalPrice())
                    .orderStatements(Collections.singletonList(orderStatement)) // todo : ????????? ???????????? ?????????????
                    .build();
        }

        public GroupedOrderStatement update(final Order order) {
            Order.OrderLinesSummary orderLinesSummary = order.getOrderLinesSummary();
            OrderStatement orderStatement = OrderStatement.builder()
                    .orderNumber(order.getOrderNumber())
                    .orderedProductNameAndQuantities(orderLinesSummary.getOrderedProductNameAndQuantities())
                    .calculatedPrice(orderLinesSummary.getTotalPrice())
                    .build();
            List<OrderStatement> updatedOrderStatements = new ArrayList<>(this.orderStatements);
            updatedOrderStatements.add(orderStatement);
            return builder()
                    .groupedOrdererMemberId(this.groupedOrdererMemberId)
                    .groupedOrdererName(this.groupedOrdererName)
                    .totalPayablePrice(this.totalPayablePrice + orderLinesSummary.getTotalPrice())
                    .orderStatements(updatedOrderStatements)
                    .build();
        }

        @Builder
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class OrderStatement {
            @Getter private final String orderNumber;
            @Getter private final String orderedProductNameAndQuantities;
            @Getter private final int calculatedPrice;
        }
    }
}
