package com.aws.peach.infrastructure.kafka;

import com.aws.peach.domain.support.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class KafkaMessageProducerFactory {

    public <K,V> MessageProducer<K,V> create(final String bootstrapServers,
                                             final String topic) {

        Map<String, Object> configProps = createProducerConfigMap(bootstrapServers);
        ProducerFactory<K, V> producerFactory = new DefaultKafkaProducerFactory<>(configProps);

        KafkaTemplate<K, V> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        return (key, value) -> {
            ListenableFuture<SendResult<K, V>> future = kafkaTemplate.send(topic, key, value);
            future.addCallback(new KafkaSendCallback<K, V>(){
                @Override
                public void onSuccess(SendResult<K, V> result) {
                    log.info("Sent message=[ {} ] with offset=[ {} ]", result.getProducerRecord().value(), result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(@NotNull KafkaProducerException ex) {
                    log.error("Unable to deliver message: exception=[ {} ], message=[ {} ]",
                            ex.getMessage(), ex.getFailedProducerRecord());
                }
            });
            return "ok";
        };
    }

    @NotNull
    private Map<String, Object> createProducerConfigMap(String bootstrapServers) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // by default, acks=1 && error retry enabled
//        configProps.put(ProducerConfig.ACKS_CONFIG, 1);
//        configProps.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
//        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);
        // by default, error retry restricted by delivery timeout
//        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30_000);
//        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120_000);

        return configProps;
    }
}
