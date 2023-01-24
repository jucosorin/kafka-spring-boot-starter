package com.jucosorin.kafka.spring.boot.starter.core.sample.listener;

import com.jucosorin.kafka.intercepted.AutoConfigureInterceptedKafkaConsumerRecords;
import com.jucosorin.kafka.intercepted.records.InterceptedKafkaConsumerRecords;
import com.jucosorin.kafka.spring.boot.starter.core.sample.support.TopicCreationConfiguration;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Import({
    TopicCreationConfiguration.class})
@Testcontainers
@DirtiesContext
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureInterceptedKafkaConsumerRecords
abstract class ListenerTestBase {

  @Autowired
  KafkaTemplate<Object, Object> kafkaTemplate;

  @Autowired
  InterceptedKafkaConsumerRecords interceptedKafkaConsumerRecords;
}