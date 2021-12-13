package com.aws.peach.interfaces.api;

import com.aws.peach.application.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.aws.peach.application.PaymentService.*;


@RestController
@RequestMapping("/pay")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> pay() {
        PayOrderRequest payOrderRequest = PayOrderRequest.builder().orderId("1").build();
        String paymentId = this.paymentService.payOrder(payOrderRequest);
        return ResponseEntity.ok(paymentId);
    }
}
