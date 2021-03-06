package com.aws.peach.interfaces.message;

import com.aws.peach.application.PaymentService;
import com.aws.peach.domain.payment.PaymentMessage;
import com.aws.peach.domain.support.Message;
import com.aws.peach.domain.support.MessageConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMessageConsumer implements MessageConsumer<PaymentMessage> {

    private final PaymentService paymentService;

    @Override
    public void consume(Message<PaymentMessage> message) {
        Message.Id messageId = message.getMessageId();
        PaymentService.PaymentRequestDto request = convert(message.getPayload());
        paymentService.charge(messageId, request);
    }

    private PaymentService.PaymentRequestDto convert(PaymentMessage payload) {
        return PaymentService.PaymentRequestDto.builder()
                .cardHolderName(payload.getCardHolderName())
                .cardNumber(payload.getCardNumber())
                .amount(payload.getAmount())
                .build();
    }
}
