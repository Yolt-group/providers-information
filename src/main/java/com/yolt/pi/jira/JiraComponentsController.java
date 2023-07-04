package com.yolt.pi.jira;

import com.yolt.pi.jira.dto.JiraComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "components-list", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class JiraComponentsController {

    private final JiraComponentsService jiraComponentsService;

    @GetMapping
    public CompletableFuture<List<JiraComponent>> getComponentsList() {
        return jiraComponentsService.getJiraComponentsList();
    }
}