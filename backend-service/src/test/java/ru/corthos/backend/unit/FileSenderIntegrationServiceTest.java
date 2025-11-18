package ru.corthos.backend.unit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.corthos.backend.MultipartFileUtil;
import ru.corthos.backend.service.converter.ConverterService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSenderIntegrationServiceTest {

    private static RestTemplate restTemplate;
    private static FileSenderIntegrationWrapper senderIntegrationWrapper;

    @BeforeAll
    public static void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        senderIntegrationWrapper = new FileSenderIntegrationWrapper(restTemplate);
    }

    @Test
    void createTempFile() {
        var multipartFile = MultipartFileUtil.getMultipartFile();
        var tempFile = senderIntegrationWrapper.createTempFile(multipartFile);
        assertTrue(Files.exists(tempFile));
    }

    @Test
    void createAndRemoveTempFile() throws IOException {
        var content = "content";
        var multipartFile = MultipartFileUtil.getMultipartFile();
        var tempFile = senderIntegrationWrapper.createTempFile(multipartFile);
        assertTrue(Files.exists(tempFile));
        assertEquals(content, Files.readString(tempFile));
        senderIntegrationWrapper.removeTempFile(tempFile);
        assertFalse(Files.exists(tempFile));
    }

    @Test
    void createHeadersForRequest() {
        var httpHeaders = senderIntegrationWrapper.createHeadersForRequest();
        assertEquals(1, httpHeaders.size());
        assertEquals(MediaType.MULTIPART_FORM_DATA, httpHeaders.getContentType());
    }

    @Test
    void createBodyForRequest() {
        var key = "data";
        var tempFile = Mockito.mock(Path.class);
        var result = senderIntegrationWrapper.createBodyForRequest(tempFile, key);
        assertEquals(List.of(new FileSystemResource(tempFile)), result.get(key));
    }

    @Test
    void executeRequest() {
        var path = "/yourPath";
        var key = "yourKey";
        var multipartFile = MultipartFileUtil.getMultipartFile();
        var tempFile = senderIntegrationWrapper.createTempFile(multipartFile);
        var body = senderIntegrationWrapper.createBodyForRequest(tempFile, key);
        var httpHeaders = senderIntegrationWrapper.createHeadersForRequest();
        senderIntegrationWrapper.executeRequest(body, httpHeaders, path);
        Mockito
                .verify(restTemplate)
                .exchange(
                        path,
                        HttpMethod.POST,
                        new HttpEntity<>(body, httpHeaders),
                        byte[].class
                );
    }

    @Test
    void givenLongRequest_whenException_thenRemoveTempFile() {
        var multipartFile = MultipartFileUtil.getMultipartFile();
        var path = Path.of("testPath");
        var converterService = new ConverterService(senderIntegrationWrapper);
        var senderIntegrationWrapper = Mockito.mock(FileSenderIntegrationWrapper.class);
        Mockito.when(senderIntegrationWrapper.createTempFile(multipartFile)).thenReturn(path);
        Mockito.when(senderIntegrationWrapper.convert(multipartFile)).thenThrow(new ResourceAccessException("HTTP connect timed out"));

        try {
            converterService.cashingConvert(multipartFile);
        } catch (Exception e) {
            assertInstanceOf(ResourceAccessException.class, e);
            assertTrue(e.getMessage().contains("HTTP connect timed out"));
            assertFalse(Files.exists(path));
        }
    }

    @AfterAll
    public static void cleanup() {
        senderIntegrationWrapper.cleanup();
    }

}