package com.yolt.pi.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "yolt.pi")
public class ApplicationProperties {

    private Boolean startScheduler;
}
