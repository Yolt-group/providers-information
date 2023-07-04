package com.yolt.pi.jira.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    String key;
    List<String> components;

    @SuppressWarnings("unchecked")
    @JsonProperty("fields")
    private void unpackFields(Map<String, Object> fields) {
        List<String> result = new LinkedList<>();
        List<Map<String, String>> components = (List<Map<String, String>>) fields.get("components");
        for (Map<String, String> component : components) {
            result.add(component.get("name"));
        }
        this.components = result;
    }
}
