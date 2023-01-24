package com.jucosorin.kafka.intercepted.records;

import com.jucosorin.kafka.intercepted.AutoConfigureInterceptedKafkaConsumerRecords;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.RecordInterceptor;

/**
 * Spring autoconfiguration triggered by {@link AutoConfigureInterceptedKafkaConsumerRecords} annotation on Spring Boot tests that
 * use Spring Kafka.
 * <p>It creates a record interceptor that is applied to all Spring Kafka {@link org.springframework.kafka.listener.MessageListenerContainer}</p>s
 * created by the Spring context before its first refresh. Spring Kafka {@link org.springframework.kafka.listener.MessageListenerContainer}s created
 * at runtime will not be instrumented.
 */
@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.kafka.annotation.EnableKafka")
@Slf4j
public class InterceptedKafkaConsumerRecordsAutoConfiguration implements SmartLifecycle {

  private boolean running = false;

  private final Map<String, Set<InterceptedConsumerRecord>> interceptedRecords = new HashMap<>();
  private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  private final RecordInterceptor<Object, Object> recordInterceptor = new RecordInterceptor<>() {

    /**
     * Instrument records before they are handed to the {@link org.springframework.kafka.annotation.KafkaListener} method or to the
     * {@link org.springframework.kafka.listener.MessageListener} configured for a specific {@link org.springframework.kafka.listener.KafkaMessageListenerContainer}
     *
     * @param consumerRecord the record.
     * @param consumer the consumer.
     * @return
     */
    @Override
    public ConsumerRecord<Object, Object> intercept(@NotNull ConsumerRecord<Object, Object> consumerRecord, @NotNull Consumer<Object, Object> consumer) {
      log.debug("Before record {} was processed from topic {}", consumerRecord, consumerRecord.topic());
      return consumerRecord;
    }

    /**
     * Instrument records after they were handled by the {@link org.springframework.kafka.annotation.KafkaListener} method or by the
     * {@link org.springframework.kafka.listener.MessageListener} configured for a specific {@link org.springframework.kafka.listener.KafkaMessageListenerContainer}
     *
     * @param consumerRecord the record.
     * @param consumer the consumer.
     */
    @Override
    public void afterRecord(@NotNull ConsumerRecord<Object, Object> consumerRecord, @NotNull Consumer<Object, Object> consumer) {
      log.debug("After record {} was processed from topic {}", consumerRecord, consumerRecord.topic());
      synchronized (interceptedRecords) {
        if (!interceptedRecords.containsKey(consumerRecord.topic())) {
          Set<InterceptedConsumerRecord> consumerRecords = Collections.synchronizedSet(new HashSet<>());
          consumerRecords.add(InterceptedConsumerRecord.of(consumerRecord));
          interceptedRecords.put(consumerRecord.topic(), consumerRecords);
        } else {
          interceptedRecords.get(consumerRecord.topic()).add(InterceptedConsumerRecord.of(consumerRecord));
        }
      }
    }
  };

  public InterceptedKafkaConsumerRecordsAutoConfiguration(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
    this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
  }

  @Bean
  public InterceptedKafkaConsumerRecords interceptedKafkaConsumerRecords() {
    return new InterceptedKafkaConsumerRecords(interceptedRecords);
  }

  /**
   * Fetch all {@link org.springframework.kafka.listener.MessageListenerContainer}s and set the {@link RecordInterceptor} defined above.
   * <p>This will add the interceptor to all consumers created by the Container</p>
   * <p>In order for the interceptor to be registered, the Container must be stopped and restarted</p>
   *
   */
  @Override
  @SuppressWarnings("unchecked")
  public void start() {
    log.info("Starting KafkaConsumerInterceptedCallsConfiguration");
    log.info("Querying for registered KafkaMessageListenerContainers");
    kafkaListenerEndpointRegistry.getAllListenerContainers().stream()
        .filter(messageListenerContainer -> messageListenerContainer instanceof AbstractMessageListenerContainer<?, ?>)
        .forEach(messageListenerContainer -> {
          log.info("Found registered KafkaMessageListenerContainer {}", messageListenerContainer);
          messageListenerContainer.stop();
          log.info("Stopped container {}", messageListenerContainer);
          ((AbstractMessageListenerContainer<Object, Object>) messageListenerContainer).setRecordInterceptor(recordInterceptor);
          log.info("Applied a record interceptor {} to KafkaMessageListenerContainer {}", recordInterceptor, messageListenerContainer);
          messageListenerContainer.start();
          log.info("Restarted container {}", messageListenerContainer);
        });

    running = true;
  }

  @Override
  public void stop() {
    interceptedRecords.clear();
    running = false;
    log.debug("Stopped KafkaConsumerInterceptedCallsConfiguration");
  }

  @Override
  public boolean isRunning() {
    return running;
  }

  @Override
  public boolean isAutoStartup() {
    return true;
  }

  @Override
  public void stop(@NotNull Runnable callback) {
    SmartLifecycle.super.stop(callback);
  }

  @Override
  public int getPhase() {
    return SmartLifecycle.super.getPhase();
  }
}
