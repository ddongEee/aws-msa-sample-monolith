package com.aws.peach.application.order;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.statement.OrderStatementExporter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.aws.peach.domain.order.statement.OrderStatementExporter.*;
import static com.aws.peach.domain.order.statement.OrderStatementExporter.GroupedOrderStatement.*;

@Component
public class OrderViewService {

    private final OrderRepository  orderRepository;
    private final OrderStatementExporter orderStatementExporter;

    public OrderViewService(final OrderRepository orderRepository,
                            final OrderStatementExporter orderStatementExporter) {
        this.orderRepository = orderRepository;
        this.orderStatementExporter = orderStatementExporter;
    }

    public Optional<Order> getOrder(final Long orderId) {
        return this.orderRepository.findById(orderId);
    }

    public List<GroupedOrderStatementDto> listUnpaidOrderByDate(final LocalDate targetDate) {
        List<GroupedOrderStatement> groupedOrderStatements = orderStatementExporter.loadUnpaidGroupedOrderStatementsByDate(targetDate);
        return groupedOrderStatements.stream()
                .map(GroupedOrderStatementDto::create)
                .collect(Collectors.toList());
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GroupedOrderStatementDto {
        private final String groupedOrdererMemberId;
        private final String groupedOrdererName;
        private final int totalPayablePrice;
        private final List<OrderStatementDto> orderStatements;

        private static GroupedOrderStatementDto create(GroupedOrderStatement groupedOrderStatement) {
            return builder()
                    .groupedOrdererMemberId(groupedOrderStatement.getGroupedOrdererMemberId())
                    .groupedOrdererName(groupedOrderStatement.getGroupedOrdererName())
                    .totalPayablePrice(groupedOrderStatement.getTotalPayablePrice())
                    .orderStatements(OrderStatementDto.createList(groupedOrderStatement.getOrderStatements()))
                    .build();
        }

        @Builder
        @Getter
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class OrderStatementDto {
            private final String orderNumber;
            private final String orderedProductNameAndQuantities;
            private final int calculatedPrice;

            private static OrderStatementDto create(final OrderStatement orderStatement) {
                return builder()
                        .orderNumber(orderStatement.getOrderNumber())
                        .orderedProductNameAndQuantities(orderStatement.getOrderedProductNameAndQuantities())
                        .calculatedPrice(orderStatement.getCalculatedPrice())
                        .build();
            }

            private static List<OrderStatementDto> createList(List<OrderStatement> orderStatements) {
                return orderStatements.stream()
                        .map(OrderStatementDto::create)
                        .collect(Collectors.toList());
            }
        }
    }
}
