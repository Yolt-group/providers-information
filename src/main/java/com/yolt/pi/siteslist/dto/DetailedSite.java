package com.yolt.pi.siteslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DetailedSite {

    private UUID siteId;
    private String providerName;
    private String jiraUrl;
    private String documentationUrl;
    private String email;
}
