package com.jucosorin.kafka.intercepted.records;

import java.util.Map;
import java.util.Set;

/**
 * Class exposed as a singleton Spring bean that holds {@link org.apache.kafka.clients.consumer.ConsumerRecord}s processed by
 * Spring Kafka's {@link org.springframework.kafka.listener.MessageListenerContainer}s
 *
 */
public class InterceptedKafkaConsumerRecords {

  private Map<String, Set<InterceptedConsumerRecord>> interceptedProcessedRecords;

  public InterceptedKafkaConsumerRecords(Map<String, Set<InterceptedConsumerRecord>> interceptedProcessedRecords) {
    this.interceptedProcessedRecords = interceptedProcessedRecords;
  }

  /**
   * Get the number of intercepted records processed from a specific topic
   *
   * @param topicName The topic name
   * @return Number of intercepted records processed from topic
   */
  public int getProcessedRecordsForTopic(String topicName) {
    return interceptedProcessedRecords.get(topicName) == null ? 0 : interceptedProcessedRecords.get(topicName).size();
  }

  /**
   * Clear all intercepted processed records
   */
  protected void clearProcessedRecords() {
    synchronized (interceptedProcessedRecords) {
      interceptedProcessedRecords.clear();
    }
  }
}
