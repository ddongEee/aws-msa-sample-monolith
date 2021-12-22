package com.aws.peach.interfaces.message;

import com.aws.peach.domain.order.OrderStateChangeMessage;
import com.aws.peach.domain.support.MessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderStateChangeMessageConsumer implements MessageConsumer<OrderStateChangeMessage> {
    @Override
    public void consume(OrderStateChangeMessage message) {
        log.info("@@@@ OrderStateChangeMessage consumed : {}", message);
    }
}
