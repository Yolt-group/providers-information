package com.yolt.pi.readme;

import com.yolt.pi.readme.dto.Contact;
import com.yolt.pi.readme.dto.Readme;
import com.yolt.pi.readme.dto.Repository;
import com.yolt.pi.readme.gitlabrenderer.ReadmeRenderer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReadmeFallbackStubs {

    static List<Readme> generateReadmeFallbackStubs() {
        List<Readme> readmes = new ArrayList<>(4);

        String bbvaReadme = readFile("fallback_stubs/BBVA_ReadMe_AIS.md");
        readmes.add(new Readme(ReadmeParser.parseSiteId(bbvaReadme), ReadmeRenderer.render(bbvaReadme)));
        String boursoramaReadme = readFile("fallback_stubs/Boursorama_ReadMe_AIS.md");
        readmes.add(new Readme(ReadmeParser.parseSiteId(boursoramaReadme), ReadmeRenderer.render(boursoramaReadme)));
        String nationwideAISReadme = readFile("fallback_stubs/Nationwide_ReadMe_AIS.md");
        readmes.add(new Readme(ReadmeParser.parseSiteId(nationwideAISReadme), ReadmeRenderer.render(nationwideAISReadme)));
        String nationwidePISReadme = readFile("fallback_stubs/Nationwide_ReadMe_PIS.md");
        readmes.add(new Readme(ReadmeParser.parseSiteId(nationwidePISReadme), ReadmeRenderer.render(nationwidePISReadme)));

        return readmes;
    }

    static List<Contact> generateContactFallbackStubs() {
        List<Contact> contacts = new ArrayList<>(3);

        contacts.add(ReadmeParser.parseContact(readFile("fallback_stubs/BBVA_ReadMe_AIS.md")));
        contacts.add(ReadmeParser.parseContact(readFile("fallback_stubs/Boursorama_ReadMe_AIS.md")));
        contacts.add(ReadmeParser.parseContact(readFile("fallback_stubs/Nationwide_ReadMe_AIS.md")));

        return contacts;
    }

    static List<Repository> generateRepositoriesFallbackStubs() {
        List<Repository> repositories = new ArrayList<>(4);

        String bbvaReadme = readFile("fallback_stubs/BBVA_ReadMe_AIS.md");
        repositories.add(ReadmeParser.parseRepository(bbvaReadme));
        String boursoramaReadme = readFile("fallback_stubs/Boursorama_ReadMe_AIS.md");
        repositories.add(ReadmeParser.parseRepository(boursoramaReadme));
        String nationwideAISReadme = readFile("fallback_stubs/Nationwide_ReadMe_AIS.md");
        repositories.add(ReadmeParser.parseRepository(nationwideAISReadme));
        String nationwidePISReadme = readFile("fallback_stubs/Nationwide_ReadMe_PIS.md");
        repositories.add(ReadmeParser.parseRepository(nationwidePISReadme));

        return repositories;
    }

    private static String readFile(final String filePath) {
        try {
            File resource = new ClassPathResource(filePath).getFile();
            return new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException ioException) {
            throw new IllegalStateException("Found unexpected error while loading stub file: " + filePath);
        }
    }
}
