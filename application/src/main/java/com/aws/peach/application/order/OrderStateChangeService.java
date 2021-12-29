package com.aws.peach.application.order;

import com.aws.peach.domain.delivery.DeliveryChangeEvent;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
public class OrderStateChangeService {
    private final OrderRepository orderRepository;

    public OrderStateChangeService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void changeOrderState(DeliveryChangeEvent event) {
        if (event.isPreparing()) {
            updateOrderState(event.getOrderNo(), Order::prepare);
        } else if (event.isPackaging()) {
            updateOrderState(event.getOrderNo(), Order::pack);
        } else if (event.isShipped()) {
            updateOrderState(event.getOrderNo(), Order::ship);
        } else if (event.isDelivered()) {
            updateOrderState(event.getOrderNo(), Order::close);
        } else {
            // TODO send invalid message to DLT
            log.warn("DeliveryChangeEvent not eligible for order state change: {}", event);
        }
    }

    private void updateOrderState(String orderNo, Consumer<Order> updater) {
        Order order = orderRepository.findById(orderNo);
        if(order == null) {
            // 대상 order가 없으면 그냥 종료
            return;
        }
        updater.accept(order);
        orderRepository.save(order);
    }
}
