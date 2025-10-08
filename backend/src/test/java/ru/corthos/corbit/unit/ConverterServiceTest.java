package ru.corthos.corbit.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.ResourceAccessException;
import ru.corthos.corbit.service.ConverterService;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ConverterServiceTest {

    @Autowired
    private ConverterService converterService;

    @Test
    void givenLongRequest_whenException_thenRemoveTempFile() {
        var multipartFile = createMultipartFile();
        try {
            converterService.cashingConvert(multipartFile);
        } catch (Exception e) {
            assertTrue(e instanceof ResourceAccessException);
            assertTrue(e.getMessage().contains("HTTP connect timed out"));
            // TODO проверка на удален ли временный файл
        }
    }

    private MockMultipartFile createMultipartFile() {
        return new MockMultipartFile("test.txt", "content".getBytes(StandardCharsets.UTF_8));
    }

}
