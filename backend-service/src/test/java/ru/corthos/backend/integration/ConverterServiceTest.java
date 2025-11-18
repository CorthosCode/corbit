package ru.corthos.backend.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.corthos.backend.MultipartFileUtil;
import ru.corthos.backend.service.converter.ConverterService;
import ru.corthos.backend.unit.FileSenderIntegrationWrapper;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConverterServiceTest {

    private static final String EUGENMAYER_JODCONVERTER_REST_0_2_0 = "eugenmayer/jodconverter:rest-0.2.0";
    private static final String GOTENBERG_GOTENBERG_8 = "gotenberg/gotenberg:8";

    @Container
    private static final GenericContainer<?> converterContainer =
            new GenericContainer<>(GOTENBERG_GOTENBERG_8)
                    .withExposedPorts(3000);

    private ConverterService converterService;

    @BeforeAll
    void setUp() {
        var baseUrl = "http://" + converterContainer.getHost() + ":" + converterContainer.getMappedPort(3000);

        var restTemplate = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .connectTimeout(Duration.of(2, ChronoUnit.SECONDS))  // 2 секунд на подключение
                .readTimeout(Duration.of(3, ChronoUnit.SECONDS))    // 3 секунд на ожидание ответа
                .build();

        var integrationService = new FileSenderIntegrationWrapper(restTemplate);
        converterService = new ConverterService(integrationService);
    }

    @Test
    void cashingConvert() {
        var multipartFile = MultipartFileUtil.getMultipartFile();
        var response = converterService.cashingConvert(multipartFile);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void fastConvert() {
        var multipartFile = MultipartFileUtil.getMultipartFile();
        var response = converterService.fastConvert(multipartFile);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}