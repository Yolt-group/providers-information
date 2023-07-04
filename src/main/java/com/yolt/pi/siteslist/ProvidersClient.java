package com.yolt.pi.siteslist;

import com.yolt.pi.common.WebClientHelper;
import com.yolt.pi.siteslist.model.SitesResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProvidersClient {

    private final WebClient webClient;

    public ProvidersClient(WebClientHelper webClientHelper,
                           SitesListProperties properties) {
        this.webClient = webClientHelper.buildWebClient(properties.getBaseUrl(), false);
    }

    @Async
    public CompletableFuture<SitesResult> acquireSitesList() {
        log.info("Acquiring sites list from providers");
        CompletableFuture<SitesResult> completableFuture = new CompletableFuture<>();
        try {
            webClient.get()
                    .uri("/providers/sites-details")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().isError()) {
                            completableFuture.completeExceptionally(new IllegalStateException("Request to providers failed with status code " + clientResponse.statusCode()));
                        }
                        return clientResponse.toEntity(SitesResult.class);
                    })
                    .doOnError(e -> completableFuture.completeExceptionally(new IllegalStateException("Acquiring response body from providers failed", e)))
                    .subscribe(responseEntity -> {
                        SitesResult body = responseEntity.getBody();
                        if (body == null) {
                            completableFuture.completeExceptionally(new IllegalStateException("Acquiring response body from site-management failed"));
                        } else {
                            completableFuture.complete(body);
                        }
                    });
        } catch (Exception e) {
            completableFuture.completeExceptionally(new IllegalStateException("There was unexpected issue while collecting data from site-management", e));
        }
        return completableFuture;
    }
}
