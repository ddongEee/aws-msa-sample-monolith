package com.aws.peach.application.order;

import com.aws.peach.domain.delivery.DeliveryChangeMessage;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.OrderNumber;
import com.aws.peach.domain.support.exception.InvalidMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@Slf4j
public class OrderStateChangeService {
    private final OrderRepository orderRepository;

    public OrderStateChangeService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(transactionManager = "transactionManager")
    public void changeOrderState(DeliveryChangeMessage message) {
        OrderNumber orderNumber = new OrderNumber(message.getOrderNo());
        if (message.isPreparing()) {
            updateOrderState(orderNumber, Order::prepare);
        } else if (message.isPackaging()) {
            updateOrderState(orderNumber, Order::pack);
        } else if (message.isShipped()) {
            updateOrderState(orderNumber, Order::ship);
        } else if (message.isDelivered()) {
            updateOrderState(orderNumber, Order::close);
        } else {
            log.warn("DeliveryChangeMessage not eligible for order state change: {}", message);
            throw new InvalidMessageException();
        }
    }

    private void updateOrderState(OrderNumber orderNumber, Consumer<Order> updater) {
        Optional<Order> order = orderRepository.findById(orderNumber);
        if(order.isEmpty()) {
            // 대상 order가 없으면 그냥 종료
            return;
        }
        updater.accept(order.get());
        orderRepository.save(order.get());
    }
}
