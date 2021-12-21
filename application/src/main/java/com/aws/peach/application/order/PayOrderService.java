package com.aws.peach.application.order;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PayOrderService {
    private final OrderRepository orderRepository;

    public PayOrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<String> paid(final List<String> orderNumbers) {
        List<Order> foundOrders = orderRepository.findByOrderNumberIn(orderNumbers);
        if (orderNumbers.size() != foundOrders.size()) {
            throw new RuntimeException("요청한 주문의 개수가 일치하지 않습니다.");
        }

        List<Order> paidUpdatedOrders = foundOrders.stream()
                .peek(Order::checkedPaid)
                .collect(Collectors.toList());
        orderRepository.saveAll(paidUpdatedOrders);

        return paidUpdatedOrders.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toList());
    }
}
