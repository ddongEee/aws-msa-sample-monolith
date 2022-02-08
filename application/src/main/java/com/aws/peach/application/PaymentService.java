package com.aws.peach.application;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.exception.OrderNotExistsException;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.OrderNumber;
import com.aws.peach.domain.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Component
public class PaymentService {

    private OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Transactional(transactionManager = "transactionManager")
    public String payOrder(PayOrderRequest payOrderRequest) {
        Optional<Order> order = this.orderRepository.findById(new OrderNumber(payOrderRequest.getOrderNumber()));
        if (order.isEmpty()) {
            throw new OrderNotExistsException(payOrderRequest.getOrderNumber());
        }
        order.get().payComplete();

        Payment payment = Payment.builder().paymentId("01").build();
        return payment.getPaymentId();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static final class PayOrderRequest {
        private String orderNumber;
    }

}
