package com.aws.peach.domain.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter // for producer(jsonSerialize). can be replace with @JsonProperty("field_name") on each field
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) // for consumer(jsonDeserialize)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for producer
@ToString
public class DeliveryChangeEvent {
    private String deliveryId;
    private String orderNo;
    private Address sendingAddress;
    private Address shippingAddress;
    private String status;
    private String updatedAt;

    @JsonIgnore
    public boolean isPreparing() {
        return Status.PREPARING.name().equals(this.status);
    }

    @JsonIgnore
    public boolean isPackaging() {
        return Status.PACKAGING.name().equals(this.status);
    }

    @JsonIgnore
    public boolean isShipped() {
        return Status.SHIPPED.name().equals(this.status);
    }

    @JsonIgnore
    public boolean isDelivered() {
        return Status.DELIVERED.name().equals(this.status);
    }

    public enum Status {
        ORDER_RECEIVED, PREPARING, PACKAGING, SHIPPED, DELIVERED;
    }
}
