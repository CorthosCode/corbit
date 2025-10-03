package ru.corthos.corbit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConverterServiceTest {

    @Container
    private static final GenericContainer<?> jodconverter =
            new GenericContainer<>("eugenmayer/jodconverter:rest-0.2.0")
                    .withExposedPorts(8080);

    @Test
    void convert() {
        String baseUrl = "http://" + jodconverter.getHost() + ":" + jodconverter.getMappedPort(8080);

        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .build();

        FileSenderIntegrationService integrationService = new FileSenderIntegrationWrapper(restTemplate);
        ConverterService converterService = new ConverterService(integrationService);

        var multipartFile = createMultipartFile("content");
        ResponseEntity<byte[]> response = converterService.convert(multipartFile);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private MockMultipartFile createMultipartFile(String content) {
        return new MockMultipartFile("test.txt", content.getBytes(StandardCharsets.UTF_8));
    }

}