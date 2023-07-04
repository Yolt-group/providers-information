package com.yolt.pi.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ing.lovebird.errorhandling.ErrorDTO;
import nl.ing.lovebird.errorhandling.ExceptionHandlingService;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import static nl.ing.lovebird.errorhandling.BaseErrorConstants.GENERIC;

/**
 * Contains handlers for predefined exception.
 */
@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class ExceptionHandlers {

    private final ExceptionHandlingService service;

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handle(HttpClientErrorException ex) {
        return service.logAndConstruct(Level.WARN, GENERIC, ex);
    }
}
