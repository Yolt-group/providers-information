package com.yolt.pi.jira;

import com.yolt.pi.common.RetryException;
import com.yolt.pi.jira.dto.JiraComponent;
import com.yolt.pi.jira.model.Issue;
import com.yolt.pi.siteslist.SitesListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JiraComponentsServiceTest {

    @Mock
    private JiraConnectionService connectionService;
    @Mock
    private SitesListService sitesListService;

    private JiraComponentsService subject;

    @BeforeEach
    public void beforeEach() {
        JiraConnectionProperties jiraConnectionProperties = new JiraConnectionProperties();
        subject = new JiraComponentsService(jiraConnectionProperties, connectionService, sitesListService);
    }

    @Test()
    void testUpdate_ConnectionServiceException() {
        // Given
        Mockito.when(connectionService.acquireIssuesWithComponentsList()).thenReturn(CompletableFuture.failedFuture(new Exception()));

        // When & Then
        assertThrows(RetryException.class, () -> subject.getJiraComponentsList());
    }

    @Test()
    void testUpdate_SitesListServiceException() {
        // Given
        Mockito.when(connectionService.acquireIssuesWithComponentsList()).thenReturn(CompletableFuture.completedFuture(prepareIssuesList()));
        Mockito.when(sitesListService.getSitesNameUUIDMap()).thenReturn(CompletableFuture.failedFuture(new Exception()));

        // When & Then
        assertThrows(RetryException.class, () -> subject.getJiraComponentsList());
    }

    @Test
    void testFallback() throws Exception {
        // When
        subject.fallback(new Exception());
        CompletableFuture<List<JiraComponent>> result = subject.getJiraComponentsList();

        // Then
        assertThat(result).isCompleted();
        assertThat(result.get()).isNotEmpty();
    }

    private List<Issue> prepareIssuesList() {
        List<Issue> result = new ArrayList<>();
        //TODO: create Issue stub.

        return result;
    }
}