package com.jucosorin.kafka.spring.boot.starter;

import com.jucosorin.kafka.spring.boot.starter.properties.KafkaStarterProperties;
import com.jucosorin.kafka.spring.boot.starter.core.KafkaApplicationRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration(after = TaskSchedulingAutoConfiguration.class)
@EnableConfigurationProperties(KafkaStarterProperties.class)
@ConditionalOnClass(name = "org.springframework.kafka.annotation.EnableKafka")
@ConditionalOnProperty(value = "kafka.starter.enabled", havingValue = "true")
@Import({
    KafkaSASLAutoConfiguration.class,
    KafkaProducerAutoConfiguration.class,
    KafkaConsumerAutoConfiguration.class,
    KafkaRetryAutoConfiguration.class
})
public class KafkaAutoConfiguration {

  @Bean
  ApplicationRunner kafkaStarterRunner(KafkaStarterProperties kafkaStarterProperties) {
    return new KafkaApplicationRunner(kafkaStarterProperties.version());
  }
}
