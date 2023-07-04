package com.yolt.pi.siteslist.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisteredSite {

    String name;
    String providerKey;
    UUID id;
    List<AccountType> accountTypeWhiteList;
    List<String> availableCountries;

    public enum AccountType {
        CURRENT_ACCOUNT,
        CREDIT_CARD,
        SAVINGS_ACCOUNT,
        PREPAID_ACCOUNT,
        PENSION,
        INVESTMENT;
    }
}
