package com.yolt.pi.frontend;

import com.yolt.pi.ProvidersInformationApplication;
import com.yolt.pi.TestApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = {ProvidersInformationApplication.class, TestApp.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@AutoConfigureWireMock(stubs = "classpath:/stubs/content/", httpsPort = 0, port = 0)
class ContentServiceIntegrationTest {

    @Autowired
    private ContentService subject;

    @Test
    public void shouldReturnCorrectIcon() {
        // Given
        String iconName = "existingIcon1";

        // When
        byte[] result = subject.getIcon(iconName);

        // Then
        verify(1, getRequestedFor(urlEqualTo("/content/" + iconName)));
        assertThat(result).isNotEmpty();
    }

    @Test
    public void shouldReturnCachedIcon() {
        // Given
        String iconName = "existingIcon2";

        // When
        byte[] firstIcon = subject.getIcon(iconName);
        byte[] secondIcon = subject.getIcon(iconName);

        // Then
        verify(1, getRequestedFor(urlEqualTo("/content/" + iconName)));
        assertThat(firstIcon).isNotEmpty();
        assertThat(secondIcon).isNotEmpty();
    }

    @Test
    public void shouldHandleNotExistingIcon() {
        // Given
        String iconName = "notExistingIcon1";

        // When & Then
        assertThrows(WebClientResponseException.NotFound.class, () -> subject.getIcon(iconName));
        verify(1, getRequestedFor(urlEqualTo("/content/" + iconName)));
    }

    @Test
    public void shouldNotCacheNotExistingIcon() {
        // Given
        String iconName = "notExistingIcon2";

        // When & Then
        assertThrows(WebClientResponseException.NotFound.class, () -> subject.getIcon(iconName));
        assertThrows(WebClientResponseException.NotFound.class, () -> subject.getIcon(iconName));
        verify(2, getRequestedFor(urlEqualTo("/content/" + iconName)));
    }
}