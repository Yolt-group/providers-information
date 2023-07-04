package com.yolt.pi.jira;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "yolt.pi.jira")
public class JiraConnectionProperties {

    private long scheduleFixedRate;
    private String baseUrl;
    private boolean alwaysUseFallback;
}
