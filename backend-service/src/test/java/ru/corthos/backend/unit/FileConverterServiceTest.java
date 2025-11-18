package ru.corthos.backend.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import ru.corthos.backend.MultipartFileUtil;
import ru.corthos.backend.service.converter.gotenberg.FileConverterService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;

public class FileConverterServiceTest {

    protected static RestTemplate restTemplate;
    private static FileSenderIntegrationWrapper senderIntegrationWrapper;

    @BeforeAll
    public static void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        senderIntegrationWrapper = new FileSenderIntegrationWrapper(restTemplate);
    }

    @Test
    public void shouldCreateCorrectRequestFor_Gotenberg() {
        var path = "/forms/libreoffice/convert";
        var key = "files";
        var multipartFile = MultipartFileUtil.getMultipartFile();
        var body = senderIntegrationWrapper.createBodyForRequest(multipartFile, key);
        var httpHeaders = senderIntegrationWrapper.createHeadersForRequest();
        var converterService = new FileConverterService(restTemplate);
        var response = converterService.convert(multipartFile);

        Mockito
                .verify(restTemplate)
                .exchange(
                        eq(path),
                        eq(HttpMethod.POST),
                        eq(new HttpEntity<>(body, httpHeaders)),
                        eq(byte[].class)
                );

        assertNotNull(body.get(key));
        assertNotNull(body.get(key).getFirst());
    }

    @Test
    public void shouldCreateCorrectRequestFor_Jodconverter() {
        var path = "/lool/convert-to/pdf";
        var key = "data";
        var multipartFile = MultipartFileUtil.getMultipartFile();
        var body = senderIntegrationWrapper.createBodyForRequest(multipartFile, key);
        var httpHeaders = senderIntegrationWrapper.createHeadersForRequest();
        var converterService = new ru.corthos.backend.service.converter.jodconverter.FileConverterService(restTemplate);
        var response = converterService.convert(multipartFile);

        Mockito
                .verify(restTemplate)
                .exchange(
                        eq(path),
                        eq(HttpMethod.POST),
                        eq(new HttpEntity<>(body, httpHeaders)),
                        eq(byte[].class)
                );

        assertNotNull(body.get(key));
        assertNotNull(body.get(key).getFirst());
    }

}
