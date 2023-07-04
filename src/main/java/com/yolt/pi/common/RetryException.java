package com.yolt.pi.common;

public class RetryException extends RuntimeException {

    public RetryException(final Throwable cause) {
        super(cause);
    }
}
