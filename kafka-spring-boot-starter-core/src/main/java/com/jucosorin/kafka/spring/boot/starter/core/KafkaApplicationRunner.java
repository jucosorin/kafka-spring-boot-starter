package com.jucosorin.kafka.spring.boot.starter.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@Slf4j
public record KafkaApplicationRunner(String starterVersion) implements ApplicationRunner {

  @Override
  public void run(ApplicationArguments args) {
    log.info("Loaded Kafka Spring Boot Starter with version {}", starterVersion);
  }
}
