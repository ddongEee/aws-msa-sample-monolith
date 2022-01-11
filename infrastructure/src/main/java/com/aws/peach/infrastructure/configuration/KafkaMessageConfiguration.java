package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.delivery.DeliveryChangeEvent;
import com.aws.peach.domain.order.OrderStateChangeMessage;
import com.aws.peach.domain.support.MessageProducer;
import com.aws.peach.domain.support.MessageConsumer;
import com.aws.peach.infrastructure.kafka.KafkaInfras;
import com.aws.peach.infrastructure.kafka.KafkaMessageListenerContainerFactory;
import com.aws.peach.infrastructure.kafka.KafkaMessageProducerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = {KafkaInfras.class})
public class KafkaMessageConfiguration {
    private final KafkaMessageListenerContainerFactory listenerContainerFactory;
    private final KafkaMessageProducerFactory producerFactory;

    public KafkaMessageConfiguration(KafkaMessageListenerContainerFactory listenerContainerFactory, KafkaMessageProducerFactory producerFactory) {
        this.listenerContainerFactory = listenerContainerFactory;
        this.producerFactory = producerFactory;
    }

    /* Producer */

    @Bean
    public MessageProducer<String, OrderStateChangeMessage> orderStateChangeMessageProducer(
            final KafkaTemplate<String, OrderStateChangeMessage> kafkaTemplate,
            @Value("${kafka.topic.order-state-change}") final String topic) {

        return this.producerFactory.create(kafkaTemplate, topic);
    }

    /* Consumer */
    @Bean
    public KafkaMessageListenerContainer<String, OrderStateChangeMessage> orderStateChangeMessageListenerContainer(
            @Value("${kafka.topic.order-state-change}") final String topic,
            final MessageConsumer<OrderStateChangeMessage> messageConsumer,
            final ConsumerFactory<String, OrderStateChangeMessage> consumerFactory,
            final KafkaTemplate<String, OrderStateChangeMessage> kafkaTemplate) {

        return this.listenerContainerFactory.create(topic, messageConsumer, consumerFactory, kafkaTemplate);
    }

    @Bean
    public KafkaMessageListenerContainer<String, DeliveryChangeEvent> deliveryChangeEventListenerContainer(
            @Value("${kafka.topic.delivery-event}") final String topic,
            final MessageConsumer<DeliveryChangeEvent> messageConsumer,
            final ConsumerFactory<String, DeliveryChangeEvent> consumerFactory,
            final KafkaTemplate<String, DeliveryChangeEvent> kafkaTemplate) {

        return this.listenerContainerFactory.create(topic, messageConsumer, consumerFactory, kafkaTemplate);
    }
}
