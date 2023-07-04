package com.yolt.pi.jira.model;

import lombok.Value;

import java.util.List;

@Value
public class IssuesResult {

    int startAt;
    int maxResults;
    int total;
    List<Issue> issues;
}
