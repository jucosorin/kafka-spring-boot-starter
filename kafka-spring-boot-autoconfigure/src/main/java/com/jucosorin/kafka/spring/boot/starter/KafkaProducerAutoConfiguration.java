package com.jucosorin.kafka.spring.boot.starter;

import com.jucosorin.kafka.spring.boot.starter.properties.KafkaProducerProperties;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

@AutoConfiguration
@EnableConfigurationProperties({KafkaProducerProperties.class, KafkaProperties.class})
public class KafkaProducerAutoConfiguration {

  @Qualifier("saslJaasConfig")
  private final Map<String, String> saslJaasConfig;

  public KafkaProducerAutoConfiguration(Map<String, String> saslJaasConfig) {
    this.saslJaasConfig = saslJaasConfig;
  }

  @Bean
  @ConditionalOnMissingBean
  public ProducerFactory<Object, Object> producerFactory(KafkaProducerProperties producerProperties, KafkaProperties kafkaProperties) {
    Map<String, Object> configs = new HashMap<>(kafkaProperties.buildProducerProperties());

    configs.replace(ProducerConfig.CLIENT_ID_CONFIG, configs.get(ProducerConfig.CLIENT_ID_CONFIG) + "-" + producerProperties.clientIdSuffix());
    configs.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, producerProperties.blockTimeout());
    configs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerProperties.requestTimeout());

    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerProperties.keySerializer());
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerProperties.valueSerializer());

    configs.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, producerProperties.schemaUrl());

    configs.putAll(saslJaasConfig);

    return new DefaultKafkaProducerFactory<>(configs);
  }
}