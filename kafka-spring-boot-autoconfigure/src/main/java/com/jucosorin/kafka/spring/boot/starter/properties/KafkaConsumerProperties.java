package com.jucosorin.kafka.spring.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.starter.consumer")
public record KafkaConsumerProperties(
    int concurrency,
    int sessionTimeout,
    int maxPollInterval,
    int requestTimeout,
    int pollTimeout,
    Class<?> keyDeserializer,
    Class<?> valueDeserializer,
    String schemaUrl,
    String groupId,
    String clientIdPrefix,
    String listenerContainerId
) {

}
