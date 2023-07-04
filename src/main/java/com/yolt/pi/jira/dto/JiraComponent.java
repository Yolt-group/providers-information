package com.yolt.pi.jira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class JiraComponent {

    private UUID siteId;
    private String providerName;
    private int numberOfNotDoneIssues;
    private String jiraLink;
}
