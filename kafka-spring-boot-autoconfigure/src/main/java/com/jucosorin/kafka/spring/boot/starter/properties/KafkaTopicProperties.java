package com.jucosorin.kafka.spring.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.starter.topic")
public record KafkaTopicProperties(String name, String retryName, String dltName, int partitions, short replicationFactor) {

}
