package com.aws.peach.infrastructure.aurora;

import com.aws.peach.domain.payment.PaymentRepository;
import com.aws.peach.domain.payment.entity.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentAuroraRepository extends PaymentRepository, CrudRepository<Payment, String> {
}
