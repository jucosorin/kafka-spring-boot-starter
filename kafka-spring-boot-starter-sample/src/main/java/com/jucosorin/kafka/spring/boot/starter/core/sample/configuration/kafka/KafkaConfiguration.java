package com.jucosorin.kafka.spring.boot.starter.core.sample.configuration.kafka;

import com.jucosorin.kafka.spring.boot.starter.properties.KafkaTopicProperties;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
@EnableConfigurationProperties(KafkaTopicProperties.class)
@Slf4j
public class KafkaConfiguration {

  @Qualifier("listenerConsumer")
  private final Consumer<ConsumerRecord<?, ?>> listenerConsumer;

  public KafkaConfiguration(Consumer<ConsumerRecord<?, ?>> listenerConsumer) {
    this.listenerConsumer = listenerConsumer;
  }

  @KafkaListener(topics = "${kafka.starter.topic.name}", id = "${kafka.starter.consumer.listener-container-id}", clientIdPrefix = "${kafka.starter.consumer.client-id-prefix}")
  public void listen(@Payload ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
    listenerConsumer.accept(consumerRecord);
    acknowledgment.acknowledge();
  }
}
