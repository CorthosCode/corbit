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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.corthos.corbit.service.ConverterService;

import java.io.IOException;
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
        var multipartFile = createMultipartFile();
        var tempFile = senderIntegrationWrapper.createTempFile(multipartFile);
        assertTrue(Files.exists(tempFile));
    }

    @Test
    void createAndRemoveTempFile() throws IOException {
        var content = "content";
        var multipartFile = createMultipartFile();
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
        var path = "/lool/convert-to/pdf";
        var key = "data";
        var multipartFile = createMultipartFile();
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
        var multipartFile = createMultipartFile();

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

    private MockMultipartFile createMultipartFile() {
        return new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
    }

}