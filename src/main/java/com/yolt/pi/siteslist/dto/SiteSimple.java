package com.yolt.pi.siteslist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SiteSimple {

    private UUID siteId;
    private String providerName;
    private String providerKey;
}
