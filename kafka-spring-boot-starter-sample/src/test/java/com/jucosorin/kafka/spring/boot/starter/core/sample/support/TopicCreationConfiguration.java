package com.jucosorin.kafka.spring.boot.starter.core.sample.support;

import com.jucosorin.kafka.spring.boot.starter.properties.KafkaTopicProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableConfigurationProperties(KafkaTopicProperties.class)
public class TopicCreationConfiguration {

  @Bean
  NewTopic testTopic(KafkaTopicProperties kafkaTopicProperties) {
    return new NewTopic(kafkaTopicProperties.name(), kafkaTopicProperties.partitions(), kafkaTopicProperties.replicationFactor());
  }

  @Bean
  NewTopic testTopicRetry(KafkaTopicProperties kafkaTopicProperties) {
    return new NewTopic(kafkaTopicProperties.retryName(), kafkaTopicProperties.partitions(), kafkaTopicProperties.replicationFactor());
  }

  @Bean
  NewTopic testTopicDlq(KafkaTopicProperties kafkaTopicProperties) {
    return new NewTopic(kafkaTopicProperties.dltName(), kafkaTopicProperties.partitions(), kafkaTopicProperties.replicationFactor());
  }
}
