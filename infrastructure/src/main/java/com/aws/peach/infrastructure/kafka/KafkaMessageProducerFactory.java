package com.aws.peach.infrastructure.kafka;

import com.aws.peach.domain.support.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
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

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        ProducerFactory<K, V> producerFactory = new DefaultKafkaProducerFactory<>(configProps);

        KafkaTemplate<K, V> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        return (key, value) -> {
            ListenableFuture<SendResult<K, V>> future = kafkaTemplate.send(topic, key, value);
            // todo : onFailure 에 대해서 고려
            future.completable().thenAccept(result -> {
                log.info("!!! Sent message=[ {} ] with offset=[ {} ]", value.toString(), result.getRecordMetadata().offset());
            });
            // todo : return 값 고려
            return "done";
        };
    }
}
