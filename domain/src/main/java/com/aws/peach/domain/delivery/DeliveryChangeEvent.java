package com.aws.peach.domain.delivery;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
public class DeliveryChangeEvent {
    private final String deliveryId;
    private final String orderNo;
    private final Address sendingAddress;
    private final Address shippingAddress;
    private final String status;
    private final String updatedAt;
}
