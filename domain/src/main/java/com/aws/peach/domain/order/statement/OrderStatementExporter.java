package com.aws.peach.domain.order.statement;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.product.entity.Product;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OrderStatementExporter {
    private final OrderRepository orderRepository;

    public OrderStatementExporter(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // http://blog.tremblay.pro/2019/08/map.html
    public List<GroupedOrderStatement> loadByDate(final LocalDate targetDate) {
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
                    .orderNumber(order.getOrderNo())
                    .orderedProductNameAndQuantities(orderLinesSummary.getOrderedProductNameAndQuantities())
                    .calculatedPrice(orderLinesSummary.getTotalPrice())
                    .build();
            return builder()
                    .groupedOrdererMemberId(order.getOrdererId())
                    .groupedOrdererName(order.getOrdererName())
                    .totalPayablePrice(orderLinesSummary.getTotalPrice())
                    .orderStatements(Collections.singletonList(orderStatement)) // todo : 추가로 저장될때 상관없나?
                    .build();
        }

        public GroupedOrderStatement update(final Order order) {
            Order.OrderLinesSummary orderLinesSummary = order.getOrderLinesSummary();
            OrderStatement orderStatement = OrderStatement.builder()
                    .orderNumber(order.getOrderNo())
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
