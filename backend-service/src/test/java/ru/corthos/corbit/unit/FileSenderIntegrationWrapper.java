package ru.corthos.corbit.unit;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.corthos.corbit.service.converter.BaseConverterIntegrationImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileSenderIntegrationWrapper extends BaseConverterIntegrationImpl {

    private final List<Path> pathList = new ArrayList<>();

    public FileSenderIntegrationWrapper(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public ResponseEntity<byte[]> convert(MultipartFile file) {
        var mockPdfContent = "PDF content".getBytes();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(mockPdfContent);
    }

    @Override
    public Path createTempFile(MultipartFile file) {
        var tempFile = super.createTempFile(file);
        pathList.add(tempFile);
        return tempFile;
    }

    @Override
    public void removeTempFile(Path tempFile) {
        super.removeTempFile(tempFile);
    }

    @Override
    protected HttpHeaders createHeadersForRequest() {
        return super.createHeadersForRequest();
    }

    @Override
    protected MultiValueMap<String, Object> createBodyForRequest(Path tempFile, String key) {
        return super.createBodyForRequest(tempFile, key);
    }

    @Override
    protected MultiValueMap<String, Object> createBodyForRequest(MultipartFile file, String key) {
        return super.createBodyForRequest(file, key);
    }

    @Override
    protected ResponseEntity<byte[]> executeRequest(MultiValueMap<String, Object> body, HttpHeaders headers, String path) {
        return super.executeRequest(body, headers, path);
    }

    @Override
    protected ResponseEntity<byte[]> executeRequest(MultipartFile file, String path, String key) {
        return super.executeRequest(file, path, key);
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
