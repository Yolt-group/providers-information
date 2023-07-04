package com.yolt.pi.common;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.yolt.pi.MemoryAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class RetryUtilTest {

    private MemoryAppender memoryAppender;

    @BeforeEach
    public void beforeEach() {
        Logger logger = (Logger) LoggerFactory.getLogger(RetryUtil.class.getName());
        memoryAppender = new MemoryAppender((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.DEBUG);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @AfterEach
    private void afterEach() {
        memoryAppender.reset();
    }

    @Test
    public void testCompleteWithoutException() {
        // Given
        CompletableFuture<Void> future = new CompletableFuture<>();
        AtomicBoolean updateInProgress = new AtomicBoolean(true);

        // When
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            future.complete(null);
            RetryUtil.wrapRetryExceptionHandling(future, "Exception message", updateInProgress);
        });

        // Then
        assertThat(future).isDone();
        assertThat(future).isNotCompletedExceptionally();
        assertThat(updateInProgress.get()).isFalse();
        assertThat(memoryAppender.countEvents(Level.ERROR)).isEqualTo(0);
    }

    @Test
    public void testCompleteWithException() {
        // Given
        CompletableFuture<Void> future = new CompletableFuture<>();
        AtomicBoolean updateInProgress = new AtomicBoolean(true);
        String exceptionMessage = "Exception message";

        // When
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            future.completeExceptionally(new Exception());
            assertThrows(RetryException.class,
                    () -> RetryUtil.wrapRetryExceptionHandling(future, exceptionMessage, updateInProgress));
        });

        // Then
        assertThat(future).isDone();
        assertThat(future).isCompletedExceptionally();
        assertThat(updateInProgress.get()).isFalse();
        assertThat(memoryAppender.contains(exceptionMessage, Level.ERROR)).isTrue();
    }
}