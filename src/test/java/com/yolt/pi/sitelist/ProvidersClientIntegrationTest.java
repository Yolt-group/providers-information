package com.yolt.pi.sitelist;

import com.yolt.pi.ProvidersInformationApplication;
import com.yolt.pi.TestApp;
import com.yolt.pi.siteslist.ProvidersClient;
import com.yolt.pi.siteslist.model.RegisteredSite;
import com.yolt.pi.siteslist.model.SitesResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


@SpringBootTest(classes = {ProvidersInformationApplication.class, TestApp.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@AutoConfigureWireMock(stubs = "classpath:/stubs/sitelist/", httpsPort = 0, port = 0)
class ProvidersClientIntegrationTest {

    @Autowired
    private ProvidersClient subject;

    @Test
    public void shouldReturnCorrectData() {
        // When
        CompletableFuture<SitesResult> result = subject.acquireSitesList();

        // Then
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            List<RegisteredSite> sites = result.get().getAisSiteDetails();
            assertThat(sites).overridingErrorMessage("Received invalid amount of sites").hasSize(2);

            assertThat(sites.get(0).getId().toString()).isEqualTo("7670247e-323e-4275-82f6-87f31119dbd3");
            assertThat(sites.get(0).getName()).isEqualTo("ABN AMRO");
            assertThat(sites.get(1).getId().toString()).isEqualTo("38463482-d554-4469-8406-24be13e6ecdc");
            assertThat(sites.get(1).getName()).isEqualTo("AG2R LA MONDIALE");
        });
    }
}