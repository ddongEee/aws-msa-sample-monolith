package com.aws.peach.domain.payment;

import com.aws.peach.domain.payment.entity.Payment;

import java.util.List;

public interface PaymentRepository {
    Payment save(Payment payment);
    List<Payment> findByMessageId(String messageId);
}
