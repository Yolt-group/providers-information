package com.yolt.pi.common;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class WebClientHelper {

    private final String ispProxyHost;
    private final Integer ispProxyPort;
    private boolean validProxy;

    public WebClientHelper(@Value("${isp.proxy.host:}") final String ispProxyHost,
                           @Value("${isp.proxy.port:}") final Integer ispProxyPort) {
        this.ispProxyHost = ispProxyHost;
        this.ispProxyPort = ispProxyPort;

        if (ispProxyHost != null && ispProxyHost.trim().length() > 0 && ispProxyPort != null && ispProxyPort > 0) {
            validProxy = true;
            log.info("Configured with proxy {}:{}", ispProxyHost, ispProxyPort);
        } else {
            validProxy = false;
            log.warn("No proxy configured for external connections");
        }
    }

    public WebClient buildWebClient(String baseUrl, boolean applyProxy) {
        HttpClient client = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(25))
                .doOnConnected(connection -> connection
                        .addHandler(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                        .addHandler(new WriteTimeoutHandler(10)));

        if (validProxy && applyProxy) {
            client = client.proxy(spec -> spec.type(ProxyProvider.Proxy.HTTP)
                    .host(ispProxyHost)
                    .port(ispProxyPort)
                    .connectTimeoutMillis(10000));
        }

        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }
}
