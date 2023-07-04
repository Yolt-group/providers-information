package com.yolt.pi.readme;

import com.yolt.pi.readme.dto.Contact;
import com.yolt.pi.readme.dto.Readme;
import com.yolt.pi.readme.dto.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ReadmeController {

    private final ReadmeService readmeService;

    @GetMapping("/readme")
    public CompletableFuture<List<Readme>> getReadme(@RequestParam(required = false) final Set<String> siteIds) {
        if (siteIds == null || siteIds.isEmpty()) {
            return readmeService.getReadmeList();
        }

        CompletableFuture<List<Readme>> resultFuture = new CompletableFuture<>();
        readmeService.getReadmeList().whenCompleteAsync((readmeList, throwable) -> {
            List<Readme> filteredResult = new ArrayList<>(siteIds.size());
            for (Readme readme : readmeList) {
                if (siteIds.contains(readme.getSiteId().toString())) {
                    filteredResult.add(readme);
                }
            }
            resultFuture.complete(filteredResult);
        });
        return resultFuture;
    }

    @GetMapping("/contacts")
    public CompletableFuture<List<Contact>> getContacts(@RequestParam(required = false) final Set<String> siteIds) {
        if (siteIds == null || siteIds.isEmpty()) {
            return readmeService.getContactsList();
        }

        CompletableFuture<List<Contact>> resultFuture = new CompletableFuture<>();
        readmeService.getContactsList().whenCompleteAsync((contacts, throwable) -> {
            if (throwable != null) {
                resultFuture.completeExceptionally(throwable);
            } else {
                List<Contact> filteredResult = new ArrayList<>(siteIds.size());
                for (Contact contact : contacts) {
                    if (siteIds.contains(contact.getSiteId().toString())) {
                        filteredResult.add(contact);
                    }
                }
                resultFuture.complete(filteredResult);
            }
        });
        return resultFuture;
    }

    @GetMapping("/repositories")
    public CompletableFuture<List<Repository>> getAllRepositories(@RequestParam(required = false) final Set<String> siteIds) {
        if (siteIds == null || siteIds.isEmpty()) {
            return readmeService.getRepositoriesList();
        }

        CompletableFuture<List<Repository>> resultFuture = new CompletableFuture<>();
        readmeService.getRepositoriesList().whenCompleteAsync((repositories, throwable) -> {
            if (throwable != null) {
                resultFuture.completeExceptionally(throwable);
            } else {
                List<Repository> filteredResult = new ArrayList<>(siteIds.size());
                for (Repository repository : repositories) {
                    if (siteIds.contains(repository.getSiteId().toString())) {
                        filteredResult.add(repository);
                    }
                }
                resultFuture.complete(filteredResult);
            }
        });
        return resultFuture;
    }
}