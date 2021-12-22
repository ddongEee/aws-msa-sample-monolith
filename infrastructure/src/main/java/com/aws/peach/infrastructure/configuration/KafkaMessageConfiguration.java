package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.delivery.DeliveryChangeEvent;
import com.aws.peach.domain.order.OrderStateChangeMessage;
import com.aws.peach.domain.support.MessageConsumer;
import com.aws.peach.infrastructure.kafka.KafkaInfras;
import com.aws.peach.infrastructure.kafka.KafkaMessageConsumerFactory;
import com.aws.peach.infrastructure.kafka.KafkaMessageProducerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = {KafkaInfras.class})
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class KafkaMessageConfiguration {
    private final KafkaMessageProducerFactory kafkaMessageProducerFactory;
    private final KafkaMessageConsumerFactory kafkaMessageConsumerFactory;

    public KafkaMessageConfiguration(final KafkaMessageProducerFactory kafkaMessageProducerFactory,
                                     final KafkaMessageConsumerFactory kafkaMessageConsumerFactory) {
        this.kafkaMessageProducerFactory = kafkaMessageProducerFactory;
        this.kafkaMessageConsumerFactory = kafkaMessageConsumerFactory;
    }

    /* Producer */

    @Bean
    public MessageProducer<String, OrderStateChangeMessage> orderStateChangeMessageProducer(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers,
                                                                                            @Value("${kafka.topic.order-state-change}") final String topic) {
        return kafkaMessageProducerFactory.create(bootstrapServers, topic);
    }

    /* Consumer */
    @Bean
    public KafkaMessageListenerContainer<String, OrderStateChangeMessage> albumMessageConsumer(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers,
                                                                                               @Value("${kafka.topic.order-state-change}") final String topic,
                                                                                               final MessageConsumer<OrderStateChangeMessage> albumEventConsumer) {
        return kafkaMessageConsumerFactory.create(
                KafkaMessageConsumerFactory.ConsumerProperties.<OrderStateChangeMessage>builder()
                        .serverUrl(bootstrapServers)
                        .topic(topic)
                        .groupId("aws-vodservice") // todo : 주입받기
                        .messageType(OrderStateChangeMessage.class)
                        .messageConsumer(albumEventConsumer)
                        .build()
        );
    }
    @Bean
    public KafkaMessageListenerContainer<String, DeliveryChangeEvent> deliveryChangeEventConsumer(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers,
                                                                                                  @Value("${kafka.topic.delivery-change-event}") final String topic,
                                                                                                  @Value("${kafka.topic.delivery-change-event-group-id}") final String groupId,
                                                                                                  final MessageConsumer<DeliveryChangeEvent> deliveryChangeEventConsumer) {

        return kafkaMessageConsumerFactory.create(
                KafkaMessageConsumerFactory.ConsumerProperties.<DeliveryChangeEvent>builder()
                        .serverUrl(bootstrapServers)
                        .topic(topic)
                        .groupId(groupId)
                        .messageType(DeliveryChangeEvent.class)
                        .messageConsumer(deliveryChangeEventConsumer)
                        .build()
        );
    }
}
