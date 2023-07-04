package com.yolt.pi.jira;

import com.yolt.pi.common.WebClientHelper;
import com.yolt.pi.jira.model.Issue;
import com.yolt.pi.jira.model.IssuesResult;
import lombok.extern.slf4j.Slf4j;
import nl.ing.lovebird.secretspipeline.VaultKeys;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class JiraConnectionService {

    private static final String JIRA_SEARCH_URL = "/rest/api/3/search";
    private static final String TOKEN_NAME = "providers-jira-token";

    private final WebClient webClient;
    private final String basicAuth;

    public JiraConnectionService(VaultKeys vaultKeys,
                                 WebClientHelper webClientHelper,
                                 JiraConnectionProperties properties) {
        this.webClient = webClientHelper.buildWebClient(properties.getBaseUrl(), true);

        String basicAuth;
        try {
            byte[] bytes = vaultKeys.getPassword(TOKEN_NAME).getEncoded();
            basicAuth = new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("Didn't initialize '" + TOKEN_NAME + "' properly");
            basicAuth = null;
        }
        this.basicAuth = basicAuth;
    }

    @Async
    public CompletableFuture<List<Issue>> acquireIssuesWithComponentsList() {
        if (basicAuth == null) {
            return CompletableFuture.failedFuture(new IllegalStateException("There is no '" + TOKEN_NAME + "' initialized properly"));
        }

        log.info("Acquiring open issues with components list");
        CompletableFuture<List<Issue>> completableFuture = new CompletableFuture<>();
        try {
            getJiraComponents(completableFuture, new ArrayList<>(), 0);
        } catch (Exception e) {
            completableFuture.completeExceptionally(new IllegalStateException("There was unexpected issue while collecting data from JIRA", e));
        }
        return completableFuture;
    }

    private void getJiraComponents(CompletableFuture<List<Issue>> completableFuture,
                                   List<Issue> issueList,
                                   int startAt) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(JIRA_SEARCH_URL)
                        .queryParam("startAt", Integer.toString(startAt))
                        .queryParam("fields", "components")
                        .queryParam("jql", "project = C4PO AND component is not EMPTY AND NOT status in (Canceled, Done, \"FIX IN BANK\", \"Live Maintenance\")")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Basic " + basicAuth)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().isError()) {
                        completableFuture.completeExceptionally(new IllegalStateException("Request to Jira failed with status code " + clientResponse.statusCode()));
                    }
                    return clientResponse.toEntity(IssuesResult.class);
                })
                .doOnError(e -> completableFuture.completeExceptionally(new IllegalStateException("Acquiring response body from Jira failed", e)))
                .subscribe(responseEntity -> {
                    IssuesResult body = responseEntity.getBody();
                    if (body == null) {
                        completableFuture.completeExceptionally(new IllegalStateException("Acquiring response body from Jira failed"));
                    } else {
                        issueList.addAll(body.getIssues());
                        if (issueList.size() < body.getTotal()) {
                            getJiraComponents(completableFuture, issueList, startAt + body.getMaxResults());
                        } else {
                            completableFuture.complete(issueList);
                        }
                    }
                });
    }
}
