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
        // TODO: 일단 SHIPPED 상태만 구현
        if ("SHIPPED".equalsIgnoreCase(event.getStatus())) {
            Order order = orderRepository.findById(event.getOrderNo());
            if(order == null) {
                // 대상 order가 없으면 그냥 종료
                return;
            }
            order.ship();
            orderRepository.save(order);
        }
    }
}
