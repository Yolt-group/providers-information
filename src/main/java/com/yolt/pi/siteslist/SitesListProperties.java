package com.yolt.pi.siteslist;

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
@ConfigurationProperties(prefix = "yolt.pi.siteslist")
public class SitesListProperties {

    @NotEmpty
    private String baseUrl;
    @NotEmpty
    private String iconInternalUrl;
    private long scheduleFixedRate;
    private boolean alwaysUseFallback;
}
