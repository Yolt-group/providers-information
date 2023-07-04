package com.yolt.pi.siteslist;

import com.yolt.pi.common.Retry;
import com.yolt.pi.siteslist.dto.SiteExtended;
import com.yolt.pi.siteslist.dto.SiteSimple;
import com.yolt.pi.siteslist.model.RegisteredPisSite;
import com.yolt.pi.siteslist.model.RegisteredSite;
import com.yolt.pi.siteslist.model.RegisteredSite.AccountType;
import com.yolt.pi.siteslist.model.SitesResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yolt.pi.common.RetryUtil.wrapRetryExceptionHandling;
import static com.yolt.pi.siteslist.model.RegisteredSite.AccountType.*;

@Service
@AllArgsConstructor
@Slf4j
public class SitesListService implements Retry {

    private static final Set<String> SCRAPERS = Set.of("BUDGET_INSIGHT", "SALTEDGE", "YODLEE");
    private static final Map<AccountType, String> ACCOUNT_TYPE_TO_READABLE_MAP = new EnumMap<>(AccountType.class);
    static {
        ACCOUNT_TYPE_TO_READABLE_MAP.put(CURRENT_ACCOUNT, "Current accounts");
        ACCOUNT_TYPE_TO_READABLE_MAP.put(SAVINGS_ACCOUNT, "Savings");
        ACCOUNT_TYPE_TO_READABLE_MAP.put(CREDIT_CARD, "Credit Cards");
        ACCOUNT_TYPE_TO_READABLE_MAP.put(PENSION, "Pensions");
        ACCOUNT_TYPE_TO_READABLE_MAP.put(PREPAID_ACCOUNT, "Prepaid");
        ACCOUNT_TYPE_TO_READABLE_MAP.put(INVESTMENT, "Investments");
    }

    private final AtomicBoolean updateInProgress = new AtomicBoolean(false);
    private final CompletableFuture<List<SiteSimple>> sitesSimpleList = new CompletableFuture<>();
    private final CompletableFuture<List<SiteExtended>> sitesExtendedList = new CompletableFuture<>();
    private final CompletableFuture<Map<String, UUID>> sitesNameUUIDMap = new CompletableFuture<>();
    private final CompletableFuture<Map<UUID, String>> uuidSitesNameMap = new CompletableFuture<>();

    private final SitesListProperties sitesListProperties;
    private final ProvidersClient siteManagementConnectionService;

    @Override
    public void update() {
        if (sitesListProperties.isAlwaysUseFallback()) {
            log.info("Forcing usage of fallback");
            fallback(new Exception());
            return;
        }

        log.info("Trying to update sites lists");
        if (updateInProgress.get()) {
            log.info("Update of sites lists is already in progress");
            return;
        }
        updateInProgress.set(true);

        wrapRetryExceptionHandling(siteManagementConnectionService.acquireSitesList()
                        .whenCompleteAsync((sites, throwable) -> {
                            if (throwable == null) {
                                mapSites(sites);
                                log.info("Successfully updated sites lists");
                                updateInProgress.set(false);
                            }
                        }).orTimeout(1, TimeUnit.MINUTES),
                "Failed to update sites lists",
                updateInProgress);
    }

    @Override
    public void fallback(final Exception e) {
        log.warn("Using fallback to update sites lists");
        sitesSimpleList.complete(SitesListFallbackStubs.generateSiteSimpleFallbackStubs());
        sitesExtendedList.complete(SitesListFallbackStubs.generateSiteExtendedFallbackStubs());
        sitesNameUUIDMap.complete(SitesListFallbackStubs.generateSitesNameUUIDMapFallbackStubs());
        uuidSitesNameMap.complete(SitesListFallbackStubs.generateUUIDSitesNameMapFallbackStubs());
    }

    @Override
    public long scheduleFixedRate() {
        return sitesListProperties.getScheduleFixedRate();
    }

    private void mapSites(SitesResult sitesResult) {
        List<RegisteredSite> aisSites = sitesResult.getAisSiteDetails();
        Map<UUID, List<RegisteredPisSite>> pisDetailsBySiteId = sitesResult.getPisSiteDetails().stream().collect(Collectors.groupingBy(RegisteredPisSite::getId));
        List<SiteSimple> newSitesSimpleList = new ArrayList<>(aisSites.size());
        List<SiteExtended> newSitesExtendedList = new ArrayList<>(aisSites.size());
        Map<String, UUID> newSitesNameUUIDMap = new HashMap<>();
        Map<UUID, String> newUUIDSitesNameMap = new HashMap<>();

        for (RegisteredSite site : aisSites) {
            if (SCRAPERS.contains(site.getProviderKey())) {
                continue;
            }
            UUID siteId = site.getId();
            String iconURL = String.format("%s/content/images/sites/icons/%s.png", sitesListProperties.getIconInternalUrl(), siteId);
            boolean isPisSupported = pisDetailsBySiteId.getOrDefault(siteId, List.of()).stream().anyMatch(RegisteredPisSite::isSupported);
            SiteSimple siteSimple = new SiteSimple(siteId, site.getName(), site.getProviderKey());
            SiteExtended siteExtended = new SiteExtended(siteId, site.getName(), site.getProviderKey(),
                    mapAccountTypes(site.getAccountTypeWhiteList()),
                    isPisSupported ? List.of("AIS", "PIS") : List.of("AIS"),
                    site.getAvailableCountries(),
                    // maintenance mode is no longer an active features. maintenance mode might be rebuild in providers, so we keep it in this object **for now**.
                    "NONE",
                    null,
                    null,
                    iconURL);

            newSitesSimpleList.add(siteSimple);
            newSitesExtendedList.add(siteExtended);
            String siteName = site.getName().toLowerCase();
            newSitesNameUUIDMap.put(siteName, siteId);
            newUUIDSitesNameMap.put(siteId, siteName);
        }

        sitesSimpleList.complete(newSitesSimpleList);
        sitesExtendedList.complete(newSitesExtendedList);
        sitesNameUUIDMap.complete(newSitesNameUUIDMap);
        uuidSitesNameMap.complete(newUUIDSitesNameMap);
    }

    public static String mapAccountTypes(final List<AccountType> whiteListedAccountTypes) {
        if (whiteListedAccountTypes.isEmpty()) {
            return "No accounts";
        }

        final String label = whiteListedAccountTypes.stream()
                .sorted(Comparator.comparingInt(AccountType::ordinal))
                .map(ACCOUNT_TYPE_TO_READABLE_MAP::get)
                .collect(Collectors.joining(", "));

        final int lastComma = label.lastIndexOf(',');

        if (lastComma > -1) {
            return label.substring(0, lastComma) + label.substring(lastComma).replace(",", " and");
        }

        return label;
    }

    private <T> CompletableFuture<T> getCompletableFuture(CompletableFuture<T> future) {
        if (!future.isDone() && !updateInProgress.get()) {
            update();
        }
        return future;
    }

    public CompletableFuture<Map<String, UUID>> getSitesNameUUIDMap() {
        return getCompletableFuture(sitesNameUUIDMap);
    }

    public CompletableFuture<Map<UUID, String>> getUUIDSitesNameMap() {
        return getCompletableFuture(uuidSitesNameMap);
    }

    CompletableFuture<List<SiteSimple>> getSitesSimpleList() {
        return getCompletableFuture(sitesSimpleList);
    }

    CompletableFuture<List<SiteExtended>> getSitesExtendedList() {
        return getCompletableFuture(sitesExtendedList);
    }
}
