package com.yolt.pi.siteslist.model;

import lombok.Value;

import java.util.UUID;

@Value
public class RegisteredPisSite {
    UUID id;
    String providerKey;
    boolean supported;
}
