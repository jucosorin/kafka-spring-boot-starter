package com.jucosorin.kafka.spring.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.starter.producer")
public record KafkaProducerProperties(
    int blockTimeout,
    int requestTimeout,
    String clientIdSuffix,
    Class<?> keySerializer,
    Class<?> valueSerializer,
    String schemaUrl
) {
}
