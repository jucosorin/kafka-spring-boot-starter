package com.jucosorin.kafka.spring.boot.starter.core.sample.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

import com.jucosorin.kafka.intercepted.KafkaContainerInitializer;
import com.jucosorin.kafka.spring.boot.starter.properties.KafkaTopicProperties;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;

class ListenerIntegrationTest extends ListenerTestBase {

  @Container
  static final KafkaContainer kafkaContainer = new KafkaContainerInitializer();

  @Autowired
  KafkaTopicProperties topicProperties;

  @Test
  @SneakyThrows
  void GivenKafkaContainer_WhenProducerSendsMessage_ThenMessageIsReceived() {

    int totalMessages = 10;
    ArrayList<SendResult<Object, Object>> results = new ArrayList<>();
    for (int i = 0; i < totalMessages; i++) {
      results.add(kafkaTemplate.send(topicProperties.name(), String.valueOf(i), "message " + i).get());
    }

    await().atMost(10, TimeUnit.SECONDS)
        .pollDelay(100, TimeUnit.MILLISECONDS)
        .until(() -> interceptedKafkaConsumerRecords.getProcessedRecordsForTopic(topicProperties.name()), equalTo(totalMessages));

    assertThat(results).hasSize(totalMessages);
    assertThat(results).extracting("producerRecord.topic").containsOnly(topicProperties.name());
  }
}
