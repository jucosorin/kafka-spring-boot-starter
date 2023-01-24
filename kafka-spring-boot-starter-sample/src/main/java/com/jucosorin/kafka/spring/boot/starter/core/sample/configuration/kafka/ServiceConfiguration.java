package com.jucosorin.kafka.spring.boot.starter.core.sample.configuration.kafka;

import com.jucosorin.kafka.spring.boot.starter.DltConsumer;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ServiceConfiguration {

  @Bean
  public Consumer<ConsumerRecord<?, ?>> listenerConsumer() {

    return consumerRecord -> {
      log.info("Listener received record with key [{}] and value [{}] for topic [{}] on partition [{}]", consumerRecord.key(), consumerRecord.value(),
          consumerRecord.topic(), consumerRecord.partition());
    };
  }

  @Bean
  public DltConsumer<ConsumerRecord<?, ?>> dltConsumer() {

    return consumerRecord -> {
      log.info("Dlt received record with key [{}] and value [{}] for topic [{}] on partition [{}]", consumerRecord.key(), consumerRecord.value(),
          consumerRecord.topic(), consumerRecord.partition());
    };
  }
}
