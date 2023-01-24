package com.jucosorin.kafka.spring.boot.starter;

import com.jucosorin.kafka.spring.boot.starter.properties.KafkaConsumerProperties;
import com.jucosorin.kafka.spring.boot.starter.properties.KafkaRetryProperties;
import com.jucosorin.kafka.spring.boot.starter.properties.KafkaTopicProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DestinationTopic.Properties;
import org.springframework.kafka.retrytopic.RetryTopicComponentFactory;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationSupport;
import org.springframework.kafka.retrytopic.RetryTopicNamesProviderFactory;
import org.springframework.kafka.retrytopic.SuffixingRetryTopicNamesProviderFactory.SuffixingRetryTopicNamesProvider;

@AutoConfiguration
@EnableConfigurationProperties({KafkaProperties.class, KafkaConsumerProperties.class, KafkaTopicProperties.class, KafkaRetryProperties.class})
@Import(KafkaDltConfiguration.class)
public class KafkaRetryAutoConfiguration extends RetryTopicConfigurationSupport {

  private final KafkaTopicProperties kafkaTopicProperties;

  public KafkaRetryAutoConfiguration(KafkaTopicProperties kafkaTopicProperties) {
    this.kafkaTopicProperties = kafkaTopicProperties;
  }

  @Override
  protected RetryTopicComponentFactory createComponentFactory() {
    return new RetryTopicComponentFactory() {
      @Override
      public RetryTopicNamesProviderFactory retryTopicNamesProviderFactory() {
        return new CustomRetryTopicNamesProviderFactory(kafkaTopicProperties);
      }
    };
  }

  @Bean
  @ConditionalOnMissingBean
  public RetryTopicConfiguration myRetryTopic(KafkaTemplate<?, ?> template, KafkaTopicProperties topicProperties, KafkaRetryProperties retryProperties) {
    return RetryTopicConfigurationBuilder
        .newInstance()
        .fixedBackOff(retryProperties.fixedBackoff())
        .maxAttempts(retryProperties.maxAttempts())
        .useSingleTopicForFixedDelays()
        .doNotAutoCreateRetryTopics()
        .doNotRetryOnDltFailure()
        .dltHandlerMethod("dltProcessor", "processDltMessage")
        .create(template);
  }

  static class CustomRetryTopicNamesProviderFactory implements RetryTopicNamesProviderFactory {

    private final KafkaTopicProperties kafkaTopicProperties;

    public CustomRetryTopicNamesProviderFactory(KafkaTopicProperties kafkaTopicProperties) {
      this.kafkaTopicProperties = kafkaTopicProperties;
    }

    @Override
    public RetryTopicNamesProvider createRetryTopicNamesProvider(Properties properties) {
      if(properties.isMainEndpoint()) {
        return new SuffixingRetryTopicNamesProvider(properties);
      }
      else {
        return new CustomRetryTopicNamesProvider(properties, kafkaTopicProperties);
      }

    }
  }

  static class CustomRetryTopicNamesProvider extends SuffixingRetryTopicNamesProvider {

    private boolean isDlt = false;
    private final KafkaTopicProperties kafkaTopicProperties;

    public CustomRetryTopicNamesProvider(Properties properties, KafkaTopicProperties kafkaTopicProperties) {
      super(properties);
      this.kafkaTopicProperties = kafkaTopicProperties;
      if (properties.isDltTopic()) {
        this.isDlt = true;
      }
    }

    @Override
    public String getTopicName(String topic) {
      return isDlt ? kafkaTopicProperties.dltName() : kafkaTopicProperties.retryName();
    }
  }
}
