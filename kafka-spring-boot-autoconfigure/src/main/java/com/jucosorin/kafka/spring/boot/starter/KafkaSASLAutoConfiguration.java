package com.jucosorin.kafka.spring.boot.starter;

import com.jucosorin.kafka.spring.boot.starter.properties.KafkaSecurityProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({KafkaSecurityProperties.class, KafkaProperties.class})
@Slf4j
public class KafkaSASLAutoConfiguration {

  public static final String SASL_SSL = "SASL_SSL";

  @Bean
  @ConditionalOnProperty(value = "spring.kafka.security.protocol", havingValue = SASL_SSL)
  @ConditionalOnMissingBean
  public Map<String, String> saslJaasConfig(KafkaSecurityProperties kafkaSecurityProperties) {

    Map<String, String> saslConfig = new HashMap<>();

    saslConfig.put("basic.auth.credentials.source", kafkaSecurityProperties.basicAuthCredentialsSource());
    saslConfig.put("basic.auth.user.info", kafkaSecurityProperties.basicAuthUserInfo());
    saslConfig.put("sasl.mechanism", kafkaSecurityProperties.saslMechanism());
    saslConfig.put("sasl.login.callback.handler.class", kafkaSecurityProperties.saslLoginCallbackHandlerClass());
    saslConfig.put("sasl.jaas.config", kafkaSecurityProperties.saslJaasConfig());

    log.debug("Added SASL_SSL configuration for Kafka");

    return saslConfig;
  }
}
