package com.aws.peach.domain.payment.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Payment {

    @Id
    private String paymentId;
    private String messageId;
}
