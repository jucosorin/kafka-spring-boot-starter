package com.jucosorin.kafka.spring.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.starter")
public record KafkaStarterProperties(
   String version,
   boolean enabled
){}
