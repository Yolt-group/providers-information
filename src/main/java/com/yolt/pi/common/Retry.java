package com.yolt.pi.common;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

public interface Retry {
    @Retryable(value = RetryException.class, maxAttempts = 2, backoff = @Backoff(delay = 5000))
    void update();

    @Recover
    void fallback(Exception e);

    long scheduleFixedRate();
}
