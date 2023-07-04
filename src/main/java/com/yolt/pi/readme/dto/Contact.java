package com.yolt.pi.readme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Contact {

    private UUID siteId;
    private String standard;
    private String contact;
}
