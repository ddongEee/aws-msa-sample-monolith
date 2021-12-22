package com.aws.peach.application.order;

import com.aws.peach.domain.delivery.DeliveryChangeEvent;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderStateChangeService {
    private final OrderRepository orderRepository;

    public OrderStateChangeService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void changeOrderState(DeliveryChangeEvent event) {
        Order order = orderRepository.findById(event.getOrderNo());
        if(order == null) {
            return;
        }
//        order.
//        if(event.getStatus())
    }
}
