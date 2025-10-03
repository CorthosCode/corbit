package ru.corthos.corbit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileSenderIntegrationService {

    private final RestTemplate restTemplate;

    @Autowired
    public FileSenderIntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected Path createTempFile(MultipartFile file) {
        try {
            var tempFile = Files.createTempFile("temp-", file.getOriginalFilename());
            file.transferTo(tempFile);
            IO.println(String.format("TempFile - %s - created", tempFile.getFileName()));
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Невозможно создать временный файл из " + file.getName());
        }
    }

    protected HttpHeaders createHeadersForRequest() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

    protected MultiValueMap<String, Object> createBodyForRequest(Path tempFile) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("data", new FileSystemResource(tempFile));
        return body;
    }

    protected ResponseEntity<byte[]> executeRequest(MultiValueMap<String, Object> body, HttpHeaders headers) {
        return restTemplate.exchange(
                "/lool/convert-to/pdf",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                byte[].class
        );
    }

    protected void removeTempFile(Path tempFile) {
        try {
            var deleted = Files.deleteIfExists(tempFile);
            IO.println(String.format("TempFile - %s - removed - %s", tempFile.getFileName(), deleted));
        } catch (IOException exception) {
            throw new RuntimeException("Невозможно удалить временный файл из " + tempFile.getFileName());
        }
    }
}
