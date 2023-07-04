package com.yolt.pi.configuration;

import nl.ing.lovebird.errorhandling.ErrorInfo;

public enum ErrorConstants implements ErrorInfo {

    PLACEHOLDER_ERROR("001", "Placeholder");

    private final String code;
    private final String message;

    ErrorConstants(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
