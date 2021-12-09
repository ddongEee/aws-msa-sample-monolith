package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.support.MessageConsumer;
import com.aws.peach.domain.support.MessageProducer;
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
@ComponentScan(basePackageClasses = { KafkaInfras.class})
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
//    @Bean
//    public MessageProducer<String, AlbumEventMessage> albumMessageProducer(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers,
//                                                                           @Value("${kafka.topic.album-event}") final String topic) {
//        return kafkaMessageProducerFactory.create(bootstrapServers, topic);
//    }

    /* Consumer */
//    @Bean
//    public KafkaMessageListenerContainer<String, AlbumEventMessage> albumMessageConsumer(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers,
//                                                                                         @Value("${kafka.topic.album-event}") final String topic,
//                                                                                         final MessageConsumer<AlbumEventMessage> albumEventConsumer) {
//        return kafkaMessageConsumerFactory.create(
//                KafkaMessageConsumerFactory.ConsumerProperties.<AlbumEventMessage>builder()
//                        .serverUrl(bootstrapServers)
//                        .topic(topic)
//                        .groupId("aws-vodservice") // todo : 주입받기
//                        .messageType(AlbumEventMessage.class)
//                        .messageConsumer(albumEventConsumer)
//                        .build()
//        );
//    }
}
