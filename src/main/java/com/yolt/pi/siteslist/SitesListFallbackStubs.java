package com.yolt.pi.siteslist;

import com.yolt.pi.siteslist.dto.SiteExtended;
import com.yolt.pi.siteslist.dto.SiteSimple;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SitesListFallbackStubs {

    static List<SiteExtended> generateSiteExtendedFallbackStubs() {
        List<SiteExtended> sitesExtendedList = new ArrayList<>(3);

        sitesExtendedList.add(new SiteExtended(UUID.fromString("26c67b2b-91d3-491f-8121-b02fc9420101"), "BBVA (ES)", "BBVA", "Current accounts",
                 Arrays.asList("AIS"), Arrays.asList("ES"), "NONE", null, null,
                "https://ingress.team9.yolt.io/content/images/sites/icons/26c67b2b-91d3-491f-8121-b02fc9420101.png"));
        sitesExtendedList.add(new SiteExtended(UUID.fromString("2121d5c2-811b-4b7a-a12b-8f83dc9804fa"), "Boursorama", "BOURSORAMA", "Current accounts",
                 Arrays.asList("AIS"), Arrays.asList("FR"), "SCHEDULED", "2018-09-25T12:01:11", "2018-09-30T12:01:11",
                "https://ingress.team9.yolt.io/content/images/sites/icons/2121d5c2-811b-4b7a-a12b-8f83dc9804fa.png"));
        sitesExtendedList.add(new SiteExtended(UUID.fromString("217a893a-5040-4291-a6be-85790c8b65c6"), "Nationwide Building Society", "NATIONWIDE", "Current accounts and Credit Cards",
                 Arrays.asList("AIS", "PIS"), Arrays.asList("UK"), "MAINTENANCE", "2021-04-04T18:01:11", "2021-04-16T22:01:11",
                "https://ingress.team9.yolt.io/content/images/sites/icons/217a893a-5040-4291-a6be-85790c8b65c6.png"));

        return sitesExtendedList;
    }

    static List<SiteSimple> generateSiteSimpleFallbackStubs() {
        List<SiteSimple> sitesSimpleList = new ArrayList<>(3);

        for (SiteExtended siteExtendedFallbackStub : generateSiteExtendedFallbackStubs()) {
            sitesSimpleList.add(new SiteSimple(siteExtendedFallbackStub.getSiteId(), siteExtendedFallbackStub.getProviderName(), siteExtendedFallbackStub.getProviderKey()));
        }

        return sitesSimpleList;
    }

    static Map<String, UUID> generateSitesNameUUIDMapFallbackStubs() {
        Map<String, UUID> sitesNameUUIDMap = new HashMap<>();

        for (SiteExtended siteExtendedFallbackStub : generateSiteExtendedFallbackStubs()) {
            sitesNameUUIDMap.put(siteExtendedFallbackStub.getProviderName().toLowerCase(), siteExtendedFallbackStub.getSiteId());
        }

        return sitesNameUUIDMap;
    }

    static Map<UUID, String> generateUUIDSitesNameMapFallbackStubs() {
        Map<UUID, String> uuidSitesNameMap = new HashMap<>();

        for (SiteExtended siteExtendedFallbackStub : generateSiteExtendedFallbackStubs()) {
            uuidSitesNameMap.put(siteExtendedFallbackStub.getSiteId(), siteExtendedFallbackStub.getProviderName().toLowerCase());
        }

        return uuidSitesNameMap;
    }
}
