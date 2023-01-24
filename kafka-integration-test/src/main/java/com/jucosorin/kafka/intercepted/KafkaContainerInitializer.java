package com.jucosorin.kafka.intercepted;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaContainerInitializer extends KafkaContainer {

  public static final String ARCH = "aarch64";

  private static DockerImageName getImageName() {
    final String arch = System.getProperty("os.arch");

    if (ARCH.equals(arch)) {
      return DockerImageName.parse("ghcr.io/arm64-compat/confluentinc/cp-kafka:6.2.5")
          .asCompatibleSubstituteFor("confluentinc/cp-kafka");
    }
    return DockerImageName.parse("confluentinc/cp-kafka:6.2.5");
  }


  public KafkaContainerInitializer() {
    super(getImageName());
    this.withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "FALSE");
    this.start();
    System.setProperty("spring.kafka.bootstrap-servers", getBootstrapServers());
  }
}
