package com.aws.peach.domain.payment;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentMessage {
    private String cardHolderName;
    private String cardNumber;
    private int amount;
}
