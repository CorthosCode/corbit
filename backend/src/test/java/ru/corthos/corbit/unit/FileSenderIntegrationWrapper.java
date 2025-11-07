package ru.corthos.corbit.unit;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.corthos.corbit.service.converter.BaseConverterImpl;
import ru.corthos.corbit.service.gotenberg.FileSenderIntegrationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileSenderIntegrationWrapper extends BaseConverterImpl {

    private final List<Path> pathList = new ArrayList<>();

    private final FileSenderIntegrationService gotenberg;

    public FileSenderIntegrationWrapper(RestTemplate restTemplate) {
        super(restTemplate);
        gotenberg = new FileSenderIntegrationService(restTemplate);
        // TODO Если есть другой сервис, надо уметь между ними переключаться
    }

    @Override
    public ResponseEntity<byte[]> convert(MultipartFile file) {
        return gotenberg.convert(file);
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
