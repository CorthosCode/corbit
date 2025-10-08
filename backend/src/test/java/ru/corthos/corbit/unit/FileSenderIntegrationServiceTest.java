package ru.corthos.corbit.unit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSenderIntegrationServiceTest {

    protected static FileSenderIntegrationWrapper senderIntegrationWrapper;
    protected static RestTemplate restTemplate;

    @BeforeAll
    public static void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        senderIntegrationWrapper = new FileSenderIntegrationWrapper(restTemplate);
    }

    @Test
    void createTempFile() {
        var multipartFile = createMultipartFile("content");
        var tempFile = senderIntegrationWrapper.createTempFile(multipartFile);
        assertTrue(Files.exists(tempFile));
    }

    @Test
    void createAndRemoveTempFile() throws IOException {
        var content = "content";
        var multipartFile = createMultipartFile(content);
        var tempFile = senderIntegrationWrapper.createTempFile(multipartFile);
        assertTrue(Files.exists(tempFile));
        assertEquals(content, Files.readString(tempFile));
        senderIntegrationWrapper.removeTempFile(tempFile);
        assertFalse(Files.exists(tempFile));
    }

    @Test
    void createHeadersForRequest() {
        var headers = senderIntegrationWrapper.createHeadersForRequest();
        assertEquals(1, headers.size());
        assertEquals(MediaType.MULTIPART_FORM_DATA, headers.getContentType());
        executeRequest();
    }

    @Test
    void createBodyForRequest() {
        var tempFile = Mockito.mock(Path.class);
        var result = senderIntegrationWrapper.createBodyForRequest(tempFile);
        assertEquals(List.of(new FileSystemResource(tempFile)), result.get("data"));
    }

    @Test
    void executeRequest() {
        var multipartFile = createMultipartFile("content");
        var tempFile = senderIntegrationWrapper.createTempFile(multipartFile);
        var body = senderIntegrationWrapper.createBodyForRequest(tempFile);
        var httpHeaders = senderIntegrationWrapper.createHeadersForRequest();
        senderIntegrationWrapper.executeRequest(body, httpHeaders);
        Mockito
                .verify(restTemplate)
                .exchange(
                        "/lool/convert-to/pdf",
                        HttpMethod.POST,
                        new HttpEntity<>(body, httpHeaders),
                        byte[].class
                );
    }

    @AfterAll
    public static void cleanup() {
        senderIntegrationWrapper.cleanup();
    }

    private MockMultipartFile createMultipartFile(String content) {
        return new MockMultipartFile("test", content.getBytes(StandardCharsets.UTF_8));
    }

}