package com.aws.peach.application;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderViewService {

    private final OrderRepository  orderRepository;

    public OrderViewService(final OrderRepository orderRepository) {
        this.orderRepository  = orderRepository;
    }

    public Order getOrder(final String orderId) {
        return this.orderRepository.findById(orderId);
    }
}
