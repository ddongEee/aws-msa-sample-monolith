package com.aws.peach.interfaces.message;

import com.aws.peach.application.order.OrderStateChangeService;
import com.aws.peach.domain.delivery.DeliveryChangeEvent;
import com.aws.peach.domain.support.MessageConsumer;
import org.springframework.stereotype.Component;

@Component
public class DeliveryChangeEventConsumer implements MessageConsumer<DeliveryChangeEvent> {
    private final OrderStateChangeService orderStateChangeService;

    public DeliveryChangeEventConsumer(final OrderStateChangeService orderStateChangeService) {
        this.orderStateChangeService = orderStateChangeService;
    }
    @Override
    public void consume(DeliveryChangeEvent event) {
        orderStateChangeService.changeOrderState(event);
    }
}