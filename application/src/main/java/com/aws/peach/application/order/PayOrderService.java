package com.aws.peach.application.order;

import com.aws.peach.domain.order.OrderStateChangeMessage;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.OrderNumber;
import com.aws.peach.domain.support.MessageProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PayOrderService {
    private final OrderRepository orderRepository;
    private final MessageProducer<String, OrderStateChangeMessage> orderStateChangeMessageProducer;

    public PayOrderService(final OrderRepository orderRepository,
                           final MessageProducer<String, OrderStateChangeMessage> orderStateChangeMessageProducer) {
        this.orderRepository = orderRepository;
        this.orderStateChangeMessageProducer = orderStateChangeMessageProducer;
    }

    @Transactional(transactionManager = "transactionManager")
    public List<String> paid(final List<String> orderNumbers) {
        List<Order> foundOrders = orderRepository.findByOrderNumberIn(OrderNumber.ofList(orderNumbers));
        if (orderNumbers.size() != foundOrders.size()) {
            throw new RuntimeException("요청한 주문의 개수가 일치하지 않습니다.");
        }

        List<Order> paidUpdatedOrders = foundOrders.stream()
                .peek(Order::checkedPaid)
                .collect(Collectors.toList());
        orderRepository.saveAll(paidUpdatedOrders);

        paidUpdatedOrders.forEach(order -> {
            final String orderNumber = order.getOrderNumber();
            OrderStateChangeMessage orderStateChangeMessage = OrderStateChangeMessage.paidCompleted(order);
            orderStateChangeMessageProducer.send(orderNumber, orderStateChangeMessage);
        });

        return paidUpdatedOrders.stream()
                .map(Order::getOrderNumber)
                .collect(Collectors.toList());
    }
}
