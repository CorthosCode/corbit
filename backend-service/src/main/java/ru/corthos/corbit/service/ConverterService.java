package ru.corthos.corbit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.corthos.corbit.service.converter.ConvertingToPDF;

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
