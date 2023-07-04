package com.yolt.pi.configuration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.yolt.pi.MemoryAppender;
import com.yolt.pi.common.Retry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationSchedulerTest {

    private final ApplicationProperties properties = new ApplicationProperties();
    private MemoryAppender memoryAppender;

    private ApplicationScheduler subject;

    @BeforeEach
    public void beforeEach() {
        Logger logger = (Logger) LoggerFactory.getLogger(ApplicationScheduler.class.getName());
        memoryAppender = new MemoryAppender((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.DEBUG);
        logger.addAppender(memoryAppender);
        memoryAppender.start();

        subject = new ApplicationScheduler(List.of(new MockRetryService(), new MockRetryService()), properties);
    }

    @AfterEach
    private void afterEach() {
        memoryAppender.reset();
    }

    @Test
    public void testSchedulerDisabled() {
        // Given
        properties.setStartScheduler(false);

        // When
        subject.startScheduledRetryableServices();

        // Then
        assertThat(memoryAppender.countEvents(Level.INFO)).isEqualTo(0);
    }

    @Test
    public void testSchedulerEnabled() {
        // Given
        properties.setStartScheduler(true);

        // When
        subject.startScheduledRetryableServices();

        // Then
        assertThat(memoryAppender.countEvents(Level.INFO)).isEqualTo(2);
        assertThat(memoryAppender.contains("Scheduling service \"MockRetryService\" every 1 [ms]", Level.INFO)).isTrue();
    }

    private static class MockRetryService implements Retry {

        @Override
        public void update() {
            // NOP
        }

        @Override
        public void fallback(final Exception e) {
            // NOP
        }

        @Override
        public long scheduleFixedRate() {
            return 1;
        }
    }
}