package com.yolt.pi.readme.model;

import lombok.Value;

/**
 * Contains information about a provider internal documentation.
 */
@Value
public class ProviderDocumentation {
    String providerIdentifier;
    String serviceType;
    String encodedContent;
}
