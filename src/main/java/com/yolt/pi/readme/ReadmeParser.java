package com.yolt.pi.readme;

import com.yolt.pi.readme.dto.Contact;
import com.yolt.pi.readme.dto.Repository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReadmeParser {


    private static final Pattern SITE_ID_PATTERN = Pattern.compile("\\|.*Site Id.*\\|\\s*(?<siteId>\\S+)\\s*\\|");
    private static final Pattern STANDARD_PATTERN = Pattern.compile("\\|.*Standard.*\\|\\s*\\[?(?<standard>[a-zA-Z0-9_ ]*)]?(\\[\\d+])?\\s*\\|");
    private static final Pattern FORM_OF_CONTACT_PATTERN = Pattern.compile("\\|.*Contact.*\\|\\s*(?<formOfContact>.*)\\s*\\|");
    private static final Pattern REPOSITORY_PATTERN = Pattern.compile("\\|.*Repository.*\\|\\s*(?<repositoryLink>.*)\\s*\\|");
    private static final Pattern REPOSITORY_NAME_PATTERN = Pattern.compile("https://git.yolt.io/providers/(?<repositoryName>[^/]+).*");

    static UUID parseSiteId(final String markdownInput) {
        for (String readmeContentLine : markdownInput.split("\n+")) {
            String siteIdString = tryToParse(null, readmeContentLine, SITE_ID_PATTERN, "siteId");
            if (siteIdString != null) {
                return UUID.fromString(siteIdString);
            }
        }
        return null;
    }

    static Contact parseContact(final String markdownInput) {
        String siteIdString = null;
        String standard = null;
        String formOfContact = null;
        for (String readmeContentLine : markdownInput.split("\n+")) {
            siteIdString = tryToParse(siteIdString, readmeContentLine, SITE_ID_PATTERN, "siteId");
            standard = tryToParse(standard, readmeContentLine, STANDARD_PATTERN, "standard");
            formOfContact = tryToParse(formOfContact, readmeContentLine, FORM_OF_CONTACT_PATTERN, "formOfContact");
            if (siteIdString != null && standard != null && formOfContact != null) {
                break;
            }
        }

        if (siteIdString == null) {
            return null;
        }

        UUID siteId = UUID.fromString(siteIdString);
        return new Contact(siteId, formatContent(standard), formatContent(formOfContact));
    }

    static Repository parseRepository(final String markdownInput) {
        String siteIdString = null;
        String repositoryLink = null;
        for (String readmeContentLine : markdownInput.split("\n+")) {
            siteIdString = tryToParse(siteIdString, readmeContentLine, SITE_ID_PATTERN, "siteId");
            repositoryLink = tryToParse(repositoryLink, readmeContentLine, REPOSITORY_PATTERN, "repositoryLink");
            if (siteIdString != null && repositoryLink != null) {
                break;
            }
        }

        if (siteIdString == null || repositoryLink == null) {
            return null;
        }

        String repositoryName = tryToParse(null, repositoryLink, REPOSITORY_NAME_PATTERN, "repositoryName");

        UUID siteId = UUID.fromString(siteIdString);
        return new Repository(siteId, formatContent(repositoryName), formatContent(repositoryLink));
    }

    private static String tryToParse(final String currentValue,
                                     final String line,
                                     final Pattern pattern,
                                     final String groupName) {
        if (currentValue != null) {
            return currentValue;
        }

        Matcher formOfContactMatcher = pattern.matcher(line);
        if (formOfContactMatcher.matches()) {
            return formOfContactMatcher.group(groupName);
        }
        return null;
    }

    private static String formatContent(String inputContent) {
        if (inputContent == null) {
            return null;
        }
        return inputContent.trim().replaceAll("\\*\\*", "");
    }
}
