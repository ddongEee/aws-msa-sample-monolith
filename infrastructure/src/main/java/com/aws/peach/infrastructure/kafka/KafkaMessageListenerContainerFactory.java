package com.aws.peach.infrastructure.kafka;

import com.aws.peach.domain.order.exception.OrderException;
import com.aws.peach.domain.support.Message;
import com.aws.peach.domain.support.MessageConsumer;
import com.aws.peach.domain.support.exception.InvalidMessageException;
import com.aws.peach.infrastructure.configuration.support.MessageIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.serializer.StringOrBytesSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class KafkaMessageListenerContainerFactory {

    private final KafkaTemplate<String, Object> stringOrBytesTemplate;

    public KafkaMessageListenerContainerFactory(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers,
                                                @Value("${spring.kafka.producer.transaction-id-prefix}") final String transactionalIdPrefix,
                                                @Value("${spring.kafka.producer.acks}") final String acks) {
        this.stringOrBytesTemplate = stringOrBytesTemplate(bootstrapServers, transactionalIdPrefix, acks);
    }

    private KafkaTemplate<String, Object> stringOrBytesTemplate(final String bootstrapServers, final String txIdPrefix,final String acks) {
        DefaultKafkaProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(
                Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                        ProducerConfig.TRANSACTIONAL_ID_CONFIG, txIdPrefix,
                        ProducerConfig.ACKS_CONFIG, acks),
                new StringSerializer(), new StringOrBytesSerializer());
        return new KafkaTemplate<>(producerFactory);
    }

    public <M> KafkaMessageListenerContainer<String,M> create(final String topic,
                                                              final MessageConsumer<M> messageConsumer,
                                                              final ConsumerFactory<String, M> consumerFactory,
                                                              final KafkaTemplate<String, M> kafkaTemplate) {
        ContainerProperties containerProps = containerProps(topic, messageConsumer);
        KafkaMessageListenerContainer<String, M> container = new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
        container.setCommonErrorHandler(errorHandler(kafkaTemplate));
        return container;
    }

    private <M> ContainerProperties containerProps(final String topic, final MessageConsumer<M> messageConsumer) {
        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setMessageListener((MessageListener<String, M>) record -> {
            final Message.Id messageId = MessageIdUtils.parse(record.headers());
            final Message<M> message = new Message<>(messageId, record.key(), record.value());
            messageConsumer.consume(message);
        });
        return containerProps;
    }

    private <M> CommonErrorHandler errorHandler(final KafkaTemplate<String, M> kafkaTemplate) {
        Map<Class<?>, KafkaOperations<?, ?>> dltTemplates = new LinkedHashMap<>();
        dltTemplates.put(byte[].class, stringOrBytesTemplate); // to publish the byte[] from a DeserializationException
        dltTemplates.put(Bytes.class, stringOrBytesTemplate);
        dltTemplates.put(String.class, stringOrBytesTemplate);
        dltTemplates.put(Void.class, stringOrBytesTemplate);
        dltTemplates.put(Object.class, kafkaTemplate);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(dltTemplates, (record, exception) -> {
            final String topic = record.topic() + ".DLT";
            log.error("Message re-routed to '{}' ({}). Record info: {}", topic, exception.getMessage(), record);
            return new TopicPartition(topic, record.partition());
        });
        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2L));
        defaultErrorHandler.addNotRetryableExceptions(notRetryableExceptions());
        return defaultErrorHandler;
    }

    private Class<? extends Exception>[] notRetryableExceptions() {
        List<Class<? extends Exception>> list = Arrays.asList(
                InvalidMessageException.class,
                OrderException.class
        );
        return list.toArray(new Class[0]);
    }
}
