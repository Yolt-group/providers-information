package com.yolt.pi;

import nl.ing.lovebird.errorhandling.ExceptionHandlingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(TestConfiguration.class)
public class TestApp {

    @Bean
    public ExceptionHandlingService exceptionHandlingService(@Value("yolt.commons.error-handling.prefix") String prefix) {
        return new ExceptionHandlingService(prefix);
    }
}