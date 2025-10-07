package ru.corthos.corbit.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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
