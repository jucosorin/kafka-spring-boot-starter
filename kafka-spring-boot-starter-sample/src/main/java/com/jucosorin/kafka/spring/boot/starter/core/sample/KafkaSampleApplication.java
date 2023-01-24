package com.jucosorin.kafka.spring.boot.starter.core.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
public class KafkaSampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(KafkaSampleApplication.class, args);
  }
}
