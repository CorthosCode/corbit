package ru.corthos.backend.service.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ConverterService {

    private final ConvertingToPDF converting;

    @Autowired
    public ConverterService(ConvertingToPDF converting) {
        this.converting = converting;
    }

    public ResponseEntity<byte[]> cashingConvert(MultipartFile file) {
        var tempFile = converting.createTempFile(file);
        try {
            return converting.convert(file);
        } finally {
            converting.removeTempFile(tempFile);
        }
    }

    public ResponseEntity<byte[]> fastConvert(MultipartFile file) {
        return converting.convert(file);
    }

}
