package com.aws.peach.application;

import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;


@Component
public class PaymentService {

    private OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public String payOrder(PayOrderRequest payOrderRequest) {

        Order order = this.orderRepository.findById(payOrderRequest.getOrderId());

        order.payComplete();
        Payment payment = Payment.builder().paymentId("01").build();

        return payment.getPaymentId();

    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static final class PayOrderRequest {

        private String orderId;
    }

}
