package com.aws.peach.application;

import com.aws.peach.domain.payment.PaymentRepository;
import com.aws.peach.domain.payment.entity.Payment;
import com.aws.peach.domain.support.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentService {

    private PaymentRepository paymentRepository;

    @Transactional(transactionManager = "transactionManager")
    public String charge(Message.Id messageId, PaymentRequestDto request) {
        // 이미 처리한 메세지 아이디인지 확인한다
        List<Payment> existingRecords = paymentRepository.findByMessageId(messageId.getValue());
        if (!existingRecords.isEmpty()) {
            final String existingPaymentId = existingRecords.get(0).getPaymentId();
            log.info("message already processed. skipping pg transaction: ({}, {})", messageId.getValue(), existingPaymentId);
            return existingPaymentId;
        }

        // 중복되지 않은 메세지의 경우 결제 처리한다
        final String paymentId = requestPgServer(request);

        // 결제 처리 후 메세지 아이디를 DB 에 저장한다.
        // 별도의 테이블을 관리하는 대신 Payment 엔티티에 메세지 아이디를 추가하였다.
        Payment paymentRecord = Payment.builder()
                .paymentId(paymentId)
                .messageId(messageId.getValue())
                .build();
        paymentRepository.save(paymentRecord);

        return paymentId;
    }

    private String requestPgServer(PaymentRequestDto request) {
        // 외부 서비스로 결제 요청. 같은 결제를 여러번 요청하면 중복 결제가 일어난다.
        return "payment-id";
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static final class PaymentRequestDto {
        private String cardHolderName;
        private String cardNumber;
        private int amount;
    }

}
