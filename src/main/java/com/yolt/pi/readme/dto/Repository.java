package com.yolt.pi.readme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Repository {

    private UUID siteId;
    private String name;
    private String link;
}
