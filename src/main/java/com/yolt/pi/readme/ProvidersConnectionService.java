package com.yolt.pi.readme;

import com.yolt.pi.common.WebClientHelper;
import com.yolt.pi.readme.model.ProviderDocumentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProvidersConnectionService {

    private final WebClient webClient;

    public ProvidersConnectionService(WebClientHelper webClientHelper,
                                      ReadmeProperties properties) {
        this.webClient = webClientHelper.buildWebClient(properties.getBaseUrl(), false);
    }

    @Async
    public CompletableFuture<List<ProviderDocumentation>> acquireReadmeFileRaws() {
        log.info("Acquiring readmes from providers");
        CompletableFuture<List<ProviderDocumentation>> completableFuture = new CompletableFuture<>();
        try {
            webClient.get()
                    .uri("/providers/internal-documentation")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().isError()) {
                            completableFuture.completeExceptionally(new IllegalStateException("Request to providers failed with status code " + clientResponse.statusCode()));
                        }
                        return clientResponse.toEntity(ProviderDocumentation[].class);
                    })
                    .doOnError(e -> completableFuture.completeExceptionally(new IllegalStateException("Acquiring response body from providers failed", e)))
                    .subscribe(responseEntity -> {
                        ProviderDocumentation[] body = responseEntity.getBody();
                        if (body == null) {
                            completableFuture.completeExceptionally(new IllegalStateException("Acquiring response body from providers failed"));
                        } else {
                            completableFuture.complete(List.of(body));
                        }
                    });
        } catch (Exception e) {
            completableFuture.completeExceptionally(new IllegalStateException("There was unexpected issue while collecting data from providers", e));
        }
        return completableFuture;
    }
}
