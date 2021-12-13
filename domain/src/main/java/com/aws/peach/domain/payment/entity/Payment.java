package com.aws.peach.domain.payment.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Payment {
    private String paymentId;
    private String status;
    private String oderId;
}
