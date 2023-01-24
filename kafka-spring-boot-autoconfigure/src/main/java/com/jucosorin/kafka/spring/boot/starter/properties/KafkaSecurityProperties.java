package com.jucosorin.kafka.spring.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.starter.security")
public record KafkaSecurityProperties(
    String basicAuthCredentialsSource,
    String basicAuthUserInfo,
    String saslMechanism,
    String saslLoginCallbackHandlerClass,
    String saslJaasConfig
) {

}
