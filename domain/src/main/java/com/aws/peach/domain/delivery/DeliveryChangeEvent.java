package com.aws.peach.domain.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter // for producer(jsonSerialize). can be replace with @JsonProperty("field_name") on each field
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) // for consumer(jsonDeserialize)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for producer
public class DeliveryChangeEvent {
    private String deliveryId;
    private String orderNo;
    private Address sendingAddress;
    private Address shippingAddress;
    private String status;
    private String updatedAt;

    @JsonIgnore
    public boolean isDeliveredStatus() {
        return Status.DELIVERED.name().equals(this.status);
    }

    private enum Status {
        ORDER_RECEIVED, PREPARING, PACKAGING, SHIPPED, DELIVERED;
    }
}
