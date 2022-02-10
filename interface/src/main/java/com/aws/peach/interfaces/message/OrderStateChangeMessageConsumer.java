package com.aws.peach.interfaces.message;

import com.aws.peach.domain.order.OrderStateChangeMessage;
import com.aws.peach.domain.support.Message;
import com.aws.peach.domain.support.MessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderStateChangeMessageConsumer implements MessageConsumer<OrderStateChangeMessage> {
    @Override
    public void consume(Message<OrderStateChangeMessage> message) {
        log.info("@@@@ OrderStateChangeMessage consumed : {}, {}, {}",
                message.getMessageId().getValue(), message.getMessageKey(), message.getPayload());
    }
}
