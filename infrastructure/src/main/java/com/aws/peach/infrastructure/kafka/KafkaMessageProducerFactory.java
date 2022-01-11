package com.aws.peach.infrastructure.kafka;

import com.aws.peach.domain.support.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Component
public class KafkaMessageProducerFactory {

    public <M> MessageProducer<String,M> create(final KafkaTemplate<String, M> kafkaTemplate,
                                                final String topic) {
        return (key, value) -> {
            ListenableFuture<SendResult<String, M>> future = kafkaTemplate.send(topic, key, value);
            future.addCallback(new KafkaSendCallback<>(){
                @Override
                public void onSuccess(SendResult<String, M> result) {
                    log.info("Sent message=[ {} ] with offset=[ {} ]", result.getProducerRecord().value(), result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(KafkaProducerException ex) {
                    log.error("Unable to deliver message: exception=[ {} ], message=[ {} ]",
                            ex.getMessage(), ex.getFailedProducerRecord());
                }
            });
            return "ok";
        };
    }
}
