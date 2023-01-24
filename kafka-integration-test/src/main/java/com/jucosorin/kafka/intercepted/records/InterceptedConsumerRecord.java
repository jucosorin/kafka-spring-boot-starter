package com.jucosorin.kafka.intercepted.records;

import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Extend {@link org.apache.kafka.clients.consumer.Consumer} to add {@code equals} and {@code hashcode} for duplicate records
 * to be filtered inside {@link  InterceptedKafkaConsumerRecords}. This is because for retry topics, Spring Kafka sends
 * failed records twice per retry (after the backoff period) and the record interceptor would add them twice to {@link  InterceptedKafkaConsumerRecords}
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class InterceptedConsumerRecord extends ConsumerRecord<Object, Object> {

  private InterceptedConsumerRecord(ConsumerRecord<Object, Object> consumerRecord) {
    super(consumerRecord.topic(),
        consumerRecord.partition(),
        consumerRecord.offset(),
        consumerRecord.timestamp(),
        consumerRecord.timestampType(),
        consumerRecord.serializedKeySize(),
        consumerRecord.serializedValueSize(),
        consumerRecord.key(),
        consumerRecord.value(),
        consumerRecord.headers(),
        consumerRecord.leaderEpoch());
  }

  public static InterceptedConsumerRecord of(ConsumerRecord<Object, Object> consumerRecord) {
    return new InterceptedConsumerRecord(consumerRecord);
  }

  @Override
  @Include
  public String topic() {
    return super.topic();
  }

  @Override
  @Include
  public int partition() {
    return super.partition();
  }

  @Override
  @Include
  public long offset() {
    return super.offset();
  }

  @Override
  @Include
  public long timestamp() {
    return super.timestamp();
  }
}
