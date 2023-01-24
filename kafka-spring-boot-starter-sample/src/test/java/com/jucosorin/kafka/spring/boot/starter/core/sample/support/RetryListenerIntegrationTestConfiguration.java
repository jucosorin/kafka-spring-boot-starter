package com.jucosorin.kafka.spring.boot.starter.core.sample.support;

import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
@Slf4j
public class RetryListenerIntegrationTestConfiguration {

  @Bean
  @Primary
  public Consumer<ConsumerRecord<?, ?>> exceptionThrowinglistenerConsumer() {

    return consumerRecord -> {
      log.info("Listener received record with key [{}] and value [{}] for topic [{}] on partition [{}]", consumerRecord.key(), consumerRecord.value(),
          consumerRecord.topic(), consumerRecord.partition());
      throw new RuntimeException("Check retry behaviour");
    };
  }
}
