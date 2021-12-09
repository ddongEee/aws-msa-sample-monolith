package com.aws.peach.infrastructure.kafka;

import com.aws.peach.domain.support.MessageConsumer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class KafkaMessageConsumerFactory {
    public <K,M> KafkaMessageListenerContainer<K,M> create(final ConsumerProperties<M> consumerProperties) {
        ContainerProperties containerProps = new ContainerProperties(consumerProperties.getTopic());
        final MessageConsumer<M> messageConsumer = consumerProperties.getMessageConsumer();
        containerProps.setMessageListener((MessageListener<String,M>) data -> messageConsumer.consume(data.value()));

        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperties.getServerUrl());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.getGroupId());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // custom value 로써..
        DefaultKafkaConsumerFactory<K, M> cf = new DefaultKafkaConsumerFactory<K,M>(
                consumerProps, null, new ErrorHandlingDeserializer(new JsonDeserializer<>(consumerProperties.getMessageType()))
        );
        KafkaMessageListenerContainer<K, M> kvKafkaMessageListenerContainer = new KafkaMessageListenerContainer<>(cf, containerProps);
        return kvKafkaMessageListenerContainer;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ConsumerProperties<Message> {
        private final String serverUrl;
        private final String topic;
        private final String groupId;
        private final MessageConsumer<Message> messageConsumer;
        private final Class<Message> messageType;
    }
}
