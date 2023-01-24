package com.jucosorin.kafka.spring.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.starter.retry")
public record KafkaRetryProperties (int fixedBackoff, int maxAttempts) {

}
