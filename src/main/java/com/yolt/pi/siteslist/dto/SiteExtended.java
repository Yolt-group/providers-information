package com.yolt.pi.siteslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SiteExtended {

    private UUID siteId;
    private String providerName;
    private String providerKey;
    private String supportedAccounts;
    private List<String> services;
    private List<String> availableInCountries;
    private String maintenanceStatus;
    private String maintenanceFrom;
    private String maintenanceTo;
    private String iconUrl;
}
