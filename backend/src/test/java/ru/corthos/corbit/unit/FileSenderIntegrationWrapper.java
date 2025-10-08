package ru.corthos.corbit.unit;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.corthos.corbit.service.FileSenderIntegrationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileSenderIntegrationWrapper extends FileSenderIntegrationService {

    private final List<Path> pathList = new ArrayList<>();

    public FileSenderIntegrationWrapper(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected Path createTempFile(MultipartFile file) {
        var tempFile = super.createTempFile(file);
        pathList.add(tempFile);
        return tempFile;
    }

    @Override
    protected HttpHeaders createHeadersForRequest() {
        return super.createHeadersForRequest();
    }

    @Override
    protected MultiValueMap<String, Object> createBodyForRequest(Path tempFile) {
        return super.createBodyForRequest(tempFile);
    }

    @Override
    protected MultiValueMap<String, Object> createBodyForRequest(MultipartFile file) {
        return super.createBodyForRequest(file);
    }

    @Override
    protected ResponseEntity<byte[]> executeRequest(MultiValueMap<String, Object> body, HttpHeaders headers) {
        return super.executeRequest(body, headers);
    }

    @Override
    protected void removeTempFile(Path tempFile) {
        super.removeTempFile(tempFile);
    }

    public void cleanup() {
        IO.println("Очистка всех временных файлов:");
        pathList.forEach(p -> {
            try {
                boolean deleted = Files.deleteIfExists(p);
                IO.println(String.format("TempFile - %s - removed - %s", p.getFileName(), deleted));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
