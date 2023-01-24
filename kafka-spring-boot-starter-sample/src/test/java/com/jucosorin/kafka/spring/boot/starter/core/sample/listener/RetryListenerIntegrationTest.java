package com.jucosorin.kafka.spring.boot.starter.core.sample.listener;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

import com.jucosorin.kafka.intercepted.KafkaContainerInitializer;
import com.jucosorin.kafka.spring.boot.starter.properties.KafkaTopicProperties;
import com.jucosorin.kafka.spring.boot.starter.core.sample.support.RetryListenerIntegrationTestConfiguration;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;

@Import(RetryListenerIntegrationTestConfiguration.class)
class RetryListenerIntegrationTest extends ListenerTestBase {

  @Container
  static final KafkaContainer kafkaContainer = new KafkaContainerInitializer();

  @Autowired
  KafkaTopicProperties topicProperties;

  @Test
  @SneakyThrows
  void GivenKafkaContainer_WhenProducerSendsMessage_ThenMessageIsReceived() {

    kafkaTemplate.send(topicProperties.name(), "key", "message to retry").get();

    await().atMost(10, TimeUnit.SECONDS)
        .pollDelay(100, TimeUnit.MILLISECONDS)
        .until(() -> interceptedKafkaConsumerRecords.getProcessedRecordsForTopic(topicProperties.dltName()), equalTo(1));

    Assertions.assertThat(interceptedKafkaConsumerRecords.getProcessedRecordsForTopic(topicProperties.retryName())).isEqualTo(3);
  }
}
