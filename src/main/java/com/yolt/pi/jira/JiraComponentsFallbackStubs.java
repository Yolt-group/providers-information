package com.yolt.pi.jira;

import com.yolt.pi.jira.dto.JiraComponent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JiraComponentsFallbackStubs {

    static List<JiraComponent> generateFallbackStubs() {
        List<JiraComponent> jiraComponents = new ArrayList<>(3);

        jiraComponents.add(new JiraComponent(UUID.fromString("26c67b2b-91d3-491f-8121-b02fc9420101"), "BBVA", 3, generateJiraLink("BBVA")));
        jiraComponents.add(new JiraComponent(UUID.fromString("2121d5c2-811b-4b7a-a12b-8f83dc9804fa"), "Boursorama", 0, null));
        jiraComponents.add(new JiraComponent(UUID.fromString("217a893a-5040-4291-a6be-85790c8b65c6"), "Nationwide", 4, generateJiraLink("Nationwide")));

        return jiraComponents;
    }

    private static String generateJiraLink(final String componentName) {
        return "https://yolt.atlassian.net/issues/?jql=project%20%3D%20%22C4PO%22%20AND%20status%20not%20in%20(%22Live%20Maintenance%22%2C%20Done%2C%20Canceled%2C%20%22FIX%20IN%20BANK%22)%20AND%20component%20%3D%20%22" + componentName + "%22%20ORDER%20BY%20id%20DESC";
    }
}
