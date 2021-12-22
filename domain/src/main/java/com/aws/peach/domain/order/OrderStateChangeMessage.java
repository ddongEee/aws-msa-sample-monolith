package com.aws.peach.domain.order;

import com.aws.peach.domain.order.vo.OrderState;
import lombok.*;

@Getter // for producer(jsonSerialize). can be replace with @JsonProperty("field_name") on each field
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) // for consumer(jsonDeserialize)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // for producer
public class OrderStateChangeMessage {
    private String orderNumber;
    private String status;

    public static OrderStateChangeMessage paidCompleted(final String orderNumber) {
        return builder()
                .orderNumber(orderNumber)
                .status(OrderState.PAID.name())
                .build();
    }
}
