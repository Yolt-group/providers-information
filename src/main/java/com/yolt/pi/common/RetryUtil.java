package com.yolt.pi.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class RetryUtil {

    public static void wrapRetryExceptionHandling(CompletableFuture<?> future,
                                                  String exceptionMessage,
                                                  AtomicBoolean updateInProgress) {
        try {
            future.exceptionally(throwable -> {
                throw new RetryException(throwable);
            }).join();
        } catch (CompletionException e) {
            log.error(exceptionMessage, e);
            if (e.getCause() instanceof RetryException) {
                throw (RetryException) e.getCause();
            }
            throw new RetryException(e.getCause());
        } catch (Exception e) {
            throw new RetryException(e);
        } finally {
            updateInProgress.set(false);
        }
    }
}
