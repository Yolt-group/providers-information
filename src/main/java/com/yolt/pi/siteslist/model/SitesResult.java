package com.yolt.pi.siteslist.model;

import lombok.Value;

import java.util.List;

@Value
public class SitesResult {

    List<RegisteredSite> aisSiteDetails;
    List<RegisteredPisSite> pisSiteDetails;

}
