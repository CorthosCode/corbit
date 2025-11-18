package ru.corthos.backend.service.converter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface ConvertingToPDF {
    ResponseEntity<byte[]> convert(MultipartFile file);

    Path createTempFile(MultipartFile file);

    void removeTempFile(Path tempFile);
}
