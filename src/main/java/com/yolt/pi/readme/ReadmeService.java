package com.yolt.pi.readme;

import com.yolt.pi.common.Retry;
import com.yolt.pi.readme.dto.Contact;
import com.yolt.pi.readme.dto.Readme;
import com.yolt.pi.readme.dto.Repository;
import com.yolt.pi.readme.gitlabrenderer.ReadmeRenderer;
import com.yolt.pi.readme.model.ProviderDocumentation;
import com.yolt.pi.siteslist.SitesListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.yolt.pi.common.RetryUtil.wrapRetryExceptionHandling;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.CompletableFuture.allOf;

@Service
@AllArgsConstructor
@Slf4j
public class ReadmeService implements Retry {

    private final AtomicBoolean updateInProgress = new AtomicBoolean(false);
    private final CompletableFuture<List<Readme>> readmeList = new CompletableFuture<>();
    private final CompletableFuture<List<Contact>> contacts = new CompletableFuture<>();
    private final CompletableFuture<List<Repository>> repositories = new CompletableFuture<>();

    private final ReadmeProperties readmeProperties;
    private final ProvidersConnectionService connectionService;
    private final SitesListService sitesListService;

    @Override
    public void update() {
        if (readmeProperties.isAlwaysUseFallback()) {
            log.info("Forcing usage of fallback");
            fallback(new Exception());
            return;
        }

        log.info("Trying to update readme, contacts and repository list");
        if (updateInProgress.get()) {
            log.info("Update of readme, contacts and repository list is already in progress");
            return;
        }
        updateInProgress.set(true);

        CompletableFuture<List<ProviderDocumentation>> documentationsListFuture = connectionService.acquireReadmeFileRaws();
        wrapRetryExceptionHandling(documentationsListFuture, "Failed to update readme, contacts and repository list", updateInProgress);
        wrapRetryExceptionHandling(allOf(sitesListService.getUUIDSitesNameMap(), documentationsListFuture)
                        .whenCompleteAsync((aVoid, throwable) -> {
                            if (throwable == null) {
                                Map<UUID, String> uuidSitesNameMap = sitesListService.getUUIDSitesNameMap().getNow(emptyMap());
                                List<ProviderDocumentation> documentations = documentationsListFuture.getNow(emptyList());
                                updateReadmeList(uuidSitesNameMap, documentations);
                                log.info("Successfully updated readme, contacts and repository list");
                                updateInProgress.set(false);
                            }
                        }).orTimeout(1, TimeUnit.MINUTES),
                "Failed to update readme, contacts and repository list",
                updateInProgress);
    }

    @Override
    public void fallback(final Exception e) {
        log.warn("Using fallback to update readme, contacts and repository list");
        readmeList.complete(ReadmeFallbackStubs.generateReadmeFallbackStubs());
        contacts.complete(ReadmeFallbackStubs.generateContactFallbackStubs());
        repositories.complete(ReadmeFallbackStubs.generateRepositoriesFallbackStubs());
    }

    @Override
    public long scheduleFixedRate() {
        return readmeProperties.getScheduleFixedRate();
    }

    private void updateReadmeList(Map<UUID, String> uuidSitesNameMap,
                                  List<ProviderDocumentation> providerDocumentations) {
        log.info("Acquired {} readme files", providerDocumentations.size());
        List<Readme> newReadmeList = new LinkedList<>();
        List<Contact> newContacts = new LinkedList<>();
        List<Repository> newRepositories = new LinkedList<>();

        Set<UUID> invalidUUIDs = new HashSet<>();
        Set<String> invalidContacts = new HashSet<>();
        Set<String> invalidRepositories = new HashSet<>();

        for (ProviderDocumentation providerDocumentation : providerDocumentations) {
            String readmeContent = new String(Base64.getDecoder().decode(providerDocumentation.getEncodedContent()), StandardCharsets.UTF_8);
            UUID siteId = ReadmeParser.parseSiteId(readmeContent);
            if (siteId == null) {
                continue;
            }
            if (!uuidSitesNameMap.containsKey(siteId)) {
                invalidUUIDs.add(siteId);
                continue;
            }

            newReadmeList.add(new Readme(siteId, ReadmeRenderer.render(readmeContent)));

            Contact contact = ReadmeParser.parseContact(readmeContent);
            if (contact == null) {
                invalidContacts.add(uuidSitesNameMap.get(siteId));
            } else {
                newContacts.add(contact);
            }

            Repository repository = ReadmeParser.parseRepository(readmeContent);
            if (repository == null) {
                invalidRepositories.add(uuidSitesNameMap.get(siteId));
            } else {
                newRepositories.add(repository);
            }
        }

        readmeList.complete(newReadmeList);
        contacts.complete(newContacts);
        repositories.complete(newRepositories);

        if (!invalidUUIDs.isEmpty()) {
            log.warn("Found readme UUIDs not matching with site-management: {}", invalidUUIDs);
        }
        if (!invalidContacts.isEmpty()) {
            log.warn("Found readme contacts not matching with site-management: {}", invalidContacts);
        }
        if (!invalidRepositories.isEmpty()) {
            log.warn("Found readme repositories not matching with site-management: {}", invalidRepositories);
        }
    }

    public CompletableFuture<List<Readme>> getReadmeList() {
        if (!readmeList.isDone() && !updateInProgress.get()) {
            update();
        }
        return readmeList;
    }

    public CompletableFuture<List<Contact>> getContactsList() {
        if (!contacts.isDone() && !updateInProgress.get()) {
            update();
        }
        return contacts;
    }

    public CompletableFuture<List<Repository>> getRepositoriesList() {
        if (!repositories.isDone() && !updateInProgress.get()) {
            update();
        }
        return repositories;
    }
}
