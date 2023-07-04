package com.yolt.pi.frontend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
class ContentService {

    private final Map<String, byte[]> iconsMap = new ConcurrentHashMap<>();

    @Value("${yolt.pi.content.baseUrl}")
    private String contentBaseURL;

    byte[] getIcon(String iconContentPath) {
        return iconsMap.computeIfAbsent(iconContentPath, key -> {
            String iconPath = contentBaseURL + iconContentPath;
            return WebClient.create(iconPath)
                    .get()
                    .accept(MediaType.IMAGE_PNG, MediaType.IMAGE_JPEG)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        });
    }
}
