package com.jucosorin.kafka.intercepted;

import com.jucosorin.kafka.intercepted.records.InterceptedKafkaConsumerRecords;
import com.jucosorin.kafka.intercepted.records.InterceptedKafkaConsumerRecordsAutoConfiguration;
import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

/**
 * Annotation for Spring Boot integration test classes that want to get notified of all the records processed by
 * Spring Kafka {@link org.springframework.kafka.listener.MessageListenerContainer}s. This annotation autoconfigures a
 * {@link InterceptedKafkaConsumerRecords} singleton bean which can be used to query information about all {@link org.apache.kafka.clients.consumer.ConsumerRecord}s
 * received by each Kafka topic managed by a {@link org.springframework.kafka.listener.MessageListenerContainer}
 * <p>A {@link org.springframework.test.context.TestExecutionListener}</p> clears the information in the {@link InterceptedKafkaConsumerRecords} bean
 * after each test so that each test starts with an empty {@code interceptedKafkaConsumerRecords} bean.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(InterceptedKafkaConsumerRecordsAutoConfiguration.class)
public @interface AutoConfigureInterceptedKafkaConsumerRecords {

}
