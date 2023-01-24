package com.jucosorin.kafka.spring.boot.starter;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class KafkaDltConfiguration {

  private final DltConsumer<ConsumerRecord<?,?>> dltConsumer;

  public KafkaDltConfiguration(DltConsumer<ConsumerRecord<?, ?>> dltConsumer) {
    this.dltConsumer = dltConsumer;
  }

  @Bean
  public KafkaDltService dltProcessor() {
    return new KafkaDltService(dltConsumer);
  }

  static class KafkaDltService {

    private final DltConsumer<ConsumerRecord<?,?>> dltConsumer;

    public KafkaDltService(DltConsumer<ConsumerRecord<?, ?>> dltConsumer) {
      this.dltConsumer = dltConsumer;
    }

    public void processDltMessage(ConsumerRecord<?, ?> consumerRecord) {
      dltConsumer.accept(consumerRecord);
    }
  }
}
