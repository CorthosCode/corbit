package ru.corthos.corbit.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.corthos.corbit.service.ConverterService;
import ru.corthos.corbit.unit.FileSenderIntegrationWrapper;

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
    private static final GenericContainer<?> jodconverter =
            new GenericContainer<>(GOTENBERG_GOTENBERG_8)
                    .withExposedPorts(3000);

    private ConverterService converterService;

    @BeforeAll
    void setUp() {
        var baseUrl = "http://" + jodconverter.getHost() + ":" + jodconverter.getMappedPort(3000);

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
        var multipartFile = createMultipartFile();
        ResponseEntity<byte[]> response = converterService.cashingConvert(multipartFile);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void fastConvert() {
        // TODO что-то ломается когда запускаем разом и юнит и итеграционные в этом тесте
        var multipartFile = createMultipartFile();
        ResponseEntity<byte[]> response = converterService.fastConvert(multipartFile);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private MockMultipartFile createMultipartFile() {
        return new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
    }

}