package ru.corthos.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder,
                                     @Value("${converter.service.base-url}") String converterBaseUrl) {
        return builder.rootUri(converterBaseUrl)
                .connectTimeout(Duration.of(5, ChronoUnit.SECONDS))  // 5 секунд на подключение
                .readTimeout(Duration.of(10, ChronoUnit.SECONDS))    // 10 секунд на ожидание ответа
                .build();
    }
}
