package com.yolt.pi.siteslist;

import com.yolt.pi.siteslist.dto.SiteExtended;
import com.yolt.pi.siteslist.dto.SiteSimple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "sites-list", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SitesListController {

    private final SitesListService sitesListService;

    @GetMapping
    public CompletableFuture<List<SiteSimple>> getSitesSimpleList(@RequestParam(required = false) final Set<String> siteIds) {
        if (siteIds == null || siteIds.isEmpty()) {
            return sitesListService.getSitesSimpleList();
        }

        CompletableFuture<List<SiteSimple>> resultFuture = new CompletableFuture<>();
        sitesListService.getSitesSimpleList().whenCompleteAsync((siteSimpleList, throwable) -> {
            List<SiteSimple> filteredResult = new ArrayList<>(siteIds.size());
            for (SiteSimple siteSimple : siteSimpleList) {
                if (siteIds.contains(siteSimple.getSiteId().toString())) {
                    filteredResult.add(siteSimple);
                }
            }
            resultFuture.complete(filteredResult);
        });
        return resultFuture;
    }

    @GetMapping("/extended")
    public CompletableFuture<List<SiteExtended>> getSitesExtendedList(@RequestParam(required = false) final Set<String> siteIds) {
        if (siteIds == null || siteIds.isEmpty()) {
            return sitesListService.getSitesExtendedList();
        }

        CompletableFuture<List<SiteExtended>> resultFuture = new CompletableFuture<>();
        sitesListService.getSitesExtendedList().whenCompleteAsync((siteExtendedList, throwable) -> {
            List<SiteExtended> filteredResult = new ArrayList<>(siteIds.size());
            for (SiteExtended siteExtended : siteExtendedList) {
                if (siteIds.contains(siteExtended.getSiteId().toString())) {
                    filteredResult.add(siteExtended);
                }
            }
            resultFuture.complete(filteredResult);
        });
        return resultFuture;
    }
}