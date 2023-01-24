package com.jucosorin.kafka.intercepted;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaContainerInitializer extends KafkaContainer {

  private static DockerImageName getImageName() {

    return DockerImageName.parse("confluentinc/cp-kafka:7.3.1");
  }

  public KafkaContainerInitializer() {
    super(getImageName());
    this.withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "FALSE");
    this.start();
    System.setProperty("spring.kafka.bootstrap-servers", getBootstrapServers());
  }
}
