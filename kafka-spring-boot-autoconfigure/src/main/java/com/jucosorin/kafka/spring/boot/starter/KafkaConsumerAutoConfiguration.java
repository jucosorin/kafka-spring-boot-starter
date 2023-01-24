package com.jucosorin.kafka.spring.boot.starter;

import com.jucosorin.kafka.spring.boot.starter.properties.KafkaConsumerProperties;
import com.jucosorin.kafka.spring.boot.starter.properties.KafkaTopicProperties;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

@AutoConfiguration
@EnableConfigurationProperties({KafkaConsumerProperties.class, KafkaProperties.class, KafkaTopicProperties.class})
@Slf4j
public class KafkaConsumerAutoConfiguration {

  @Qualifier("saslJaasConfig")
  private final Map<String, String> saslJaasConfig;

  public KafkaConsumerAutoConfiguration(Map<String, String> saslJaasConfig) {
    this.saslJaasConfig = saslJaasConfig;
  }

  @Bean
  @ConditionalOnMissingBean
  ConsumerFactory<Object, Object> consumerFactory(KafkaConsumerProperties consumerProperties, KafkaProperties kafkaProperties) {
    Map<String, Object> configs = new HashMap<>(kafkaProperties.buildConsumerProperties());

    configs.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.groupId());
    configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerProperties.sessionTimeout());
    configs.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, consumerProperties.maxPollInterval());
    configs.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, consumerProperties.requestTimeout());

    configs.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, consumerProperties.keyDeserializer());
    configs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, consumerProperties.valueDeserializer());

    configs.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, consumerProperties.schemaUrl());
    //Use Specific Record or else you get Avro GenericRecord in the consumer payload
    configs.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

    configs.putAll(saslJaasConfig);

    return new DefaultKafkaConsumerFactory<>(configs);
  }

  @Bean
  @ConditionalOnMissingBean
  ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(ConsumerFactory<Object, Object> consumerFactory,
      KafkaConsumerProperties consumerProperties) {
    ConcurrentKafkaListenerContainerFactory<?, ?> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setConcurrency(consumerProperties.concurrency());
    factory.getContainerProperties().setPollTimeout(consumerProperties.pollTimeout());
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    factory.setContainerCustomizer(container -> container.getContainerProperties().setAuthExceptionRetryInterval(Duration.ofSeconds(10L)));

    return factory;
  }
}
