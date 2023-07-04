package com.yolt.pi.jira;

import com.yolt.pi.common.Retry;
import com.yolt.pi.jira.dto.JiraComponent;
import com.yolt.pi.jira.model.Issue;
import com.yolt.pi.siteslist.SitesListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.yolt.pi.common.RetryUtil.wrapRetryExceptionHandling;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class JiraComponentsService implements Retry {

    private static final String COMPONENT_NAME = "{{COMPONENT_NAME}}";
    private static final String LINK_TEMPLATE = "https://yolt.atlassian.net/issues/?jql=project%20%3D%20%22C4PO%22%20AND%20status%20not%20in%20(%22Live%20Maintenance%22%2C%20Done%2C%20Canceled%2C%20%22FIX%20IN%20BANK%22)%20AND%20component%20%3D%20%22" + COMPONENT_NAME + "%22%20ORDER%20BY%20id%20DESC";

    private final AtomicBoolean updateInProgress = new AtomicBoolean(false);
    private final CompletableFuture<List<JiraComponent>> jiraComponentsList = new CompletableFuture<>();

    private final JiraConnectionProperties jiraConnectionProperties;
    private final JiraConnectionService connectionService;
    private final SitesListService sitesListService;

    @Override
    public void update() {
        if (jiraConnectionProperties.isAlwaysUseFallback()) {
            log.info("Forcing usage of fallback");
            fallback(new Exception());
            return;
        }

        log.info("Trying to update components list");
        if (updateInProgress.get()) {
            log.info("Update of components list is already in progress");
            return;
        }
        updateInProgress.set(true);

        CompletableFuture<List<Issue>> issueListFuture = connectionService.acquireIssuesWithComponentsList();
        wrapRetryExceptionHandling(issueListFuture, "Failed to update components list", updateInProgress);
        wrapRetryExceptionHandling(allOf(sitesListService.getSitesNameUUIDMap(), issueListFuture)
                        .whenCompleteAsync((aVoid, throwable) -> {
                            if (throwable == null) {
                                Map<String, UUID> sitesNameUUIDMap = sitesListService.getSitesNameUUIDMap().getNow(emptyMap());
                                List<Issue> issues = issueListFuture.getNow(emptyList());
                                updateJiraComponentsList(sitesNameUUIDMap, issues);
                                log.info("Successfully updated components list");
                                updateInProgress.set(false);
                            }
                        }).orTimeout(1, TimeUnit.MINUTES),
                "Failed to update components list",
                updateInProgress);
    }

    @Override
    public void fallback(final Exception e) {
        log.warn("Using fallback to update components list");
        jiraComponentsList.complete(JiraComponentsFallbackStubs.generateFallbackStubs());
    }

    @Override
    public long scheduleFixedRate() {
        return jiraConnectionProperties.getScheduleFixedRate();
    }

    CompletableFuture<List<JiraComponent>> getJiraComponentsList() {
        if (!jiraComponentsList.isDone() && !updateInProgress.get()) {
            update();
        }
        return jiraComponentsList;
    }

    private void updateJiraComponentsList(Map<String, UUID> sitesNameUUIDMap,
                                          final List<Issue> issues) {
        log.info("Acquired {} open issues with components", issues.size());
        Map<String, Integer> componentsList = new HashMap<>();
        for (Issue issue : issues) {
            for (String component : issue.getComponents()) {
                componentsList.compute(component.toLowerCase(), (k, v) -> v == null ? 1 : v + 1);
            }
        }
        Set<String> invalidComponents = new HashSet<>();
        jiraComponentsList.complete(componentsList.entrySet()
                .stream()
                .map(entry -> {
                    String providerName = entry.getKey();
                    String link = LINK_TEMPLATE.replace(COMPONENT_NAME, providerName);
                    UUID siteId;
                    if (sitesNameUUIDMap.containsKey(providerName)) {
                        siteId = sitesNameUUIDMap.getOrDefault(providerName, null);
                    } else {
                        siteId = null;
                        invalidComponents.add(providerName);
                    }
                    return new JiraComponent(siteId, providerName, entry.getValue(), link);
                }).collect(toList()));

        if (!invalidComponents.isEmpty()) {
            log.warn("Found components not matching with site-management: {}", invalidComponents);
        }
    }
}
