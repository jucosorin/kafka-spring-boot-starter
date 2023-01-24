package com.jucosorin.kafka.intercepted.records;

import com.jucosorin.kafka.intercepted.AutoConfigureInterceptedKafkaConsumerRecords;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestContextAnnotationUtils;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Spring {@link org.springframework.test.context.TestExecutionListener} that clears all records from the {@link InterceptedKafkaConsumerRecords}
 * bean after every test.
 * <p>The listener only executes if the test is annotated with {@link AutoConfigureInterceptedKafkaConsumerRecords}.</p>
 * <p>This listener is auto registered in Spring Boot tests using the <i>META-INF/spring.factories</i> mechanism.</p>
 *
 */
@Slf4j
public class InterceptedKafkaConsumerRecordsTestExecutionListener extends AbstractTestExecutionListener {

  @Override
  public void afterTestMethod(TestContext testContext) throws Exception {
    if (annotationMissing(testContext)) {
      return;
    }
    testContext.getApplicationContext()
        .getBean(InterceptedKafkaConsumerRecords.class)
        .clearProcessedRecords();
    log.info("Cleared all intercepted consumer records from InterceptedKafkaConsumerRecords");
  }

  private boolean annotationMissing(TestContext testContext) {
    if (!TestContextAnnotationUtils.hasAnnotation(testContext.getTestClass(), AutoConfigureInterceptedKafkaConsumerRecords.class)) {
      if (log.isDebugEnabled()) {
        log.debug(
            "No @AutoConfigureKafkaConsumerInterceptedCalls annotation found on [" + testContext.getTestClass() + "]. Skipping");
      }
      return true;
    }
    return false;
  }
}
