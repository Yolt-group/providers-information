package com.yolt.pi.jira;

import com.yolt.pi.ProvidersInformationApplication;
import com.yolt.pi.TestApp;
import com.yolt.pi.jira.model.Issue;
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
@AutoConfigureWireMock(stubs = "classpath:/stubs/jira/", httpsPort = 0, port = 0)
class JiraConnectionServiceIntegrationTest {

    @Autowired
    private JiraConnectionService subject;

    @Test
    public void shouldReturnCorrectData() {
        // When
        CompletableFuture<List<Issue>> result = subject.acquireIssuesWithComponentsList();

        // Then
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            List<Issue> issues = result.get();
            assertThat(issues).overridingErrorMessage("Received invalid amount of issues").hasSize(3);

            assertThat(issues.get(0).getKey()).isEqualTo("C4PO-7866");
            assertThat(issues.get(0).getComponents()).hasSize(1);
            assertThat(issues.get(1).getKey()).isEqualTo("C4PO-7006");
            assertThat(issues.get(1).getComponents()).hasSize(6);
            assertThat(issues.get(2).getKey()).isEqualTo("C4PO-7865");
            assertThat(issues.get(2).getComponents()).hasSize(1);
        });
    }
}