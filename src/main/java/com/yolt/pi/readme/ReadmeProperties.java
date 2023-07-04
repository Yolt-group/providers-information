package com.yolt.pi.readme;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "yolt.pi.readme")
public class ReadmeProperties {

    @NotEmpty
    private String baseUrl;
    private long scheduleFixedRate;
    private boolean alwaysUseFallback;
}
