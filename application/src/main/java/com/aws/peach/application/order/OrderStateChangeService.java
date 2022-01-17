package com.aws.peach.application.order;

import com.aws.peach.domain.delivery.DeliveryChangeEvent;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.support.exception.InvalidMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@Slf4j
public class OrderStateChangeService {
    private final OrderRepository orderRepository;

    public OrderStateChangeService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void changeOrderState(DeliveryChangeEvent event) {
        Long orderNo = Long.parseLong(event.getOrderNo());
        if (event.isPreparing()) {
            updateOrderState(orderNo, Order::prepare);
        } else if (event.isPackaging()) {
            updateOrderState(orderNo, Order::pack);
        } else if (event.isShipped()) {
            updateOrderState(orderNo, Order::ship);
        } else if (event.isDelivered()) {
            updateOrderState(orderNo, Order::close);
        } else {
            log.warn("DeliveryChangeEvent not eligible for order state change: {}", event);
            throw new InvalidMessageException();
        }
    }

    private void updateOrderState(long orderNo, Consumer<Order> updater) {
        Optional<Order> order = orderRepository.findById(orderNo);
        if(order.isEmpty()) {
            // 대상 order가 없으면 그냥 종료
            return;
        }
        updater.accept(order.get());
        orderRepository.save(order.get());
    }
}
