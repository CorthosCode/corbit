package ru.corthos.corbit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ConverterService {

    private final FileSenderIntegrationService fileSenderIntegrationService;

    @Autowired
    public ConverterService(FileSenderIntegrationService fileSenderIntegrationService) {
        this.fileSenderIntegrationService = fileSenderIntegrationService;
    }

    public ResponseEntity<byte[]> cashingConvert(MultipartFile file) {
        var tempFile = fileSenderIntegrationService.createTempFile(file);

        var headers = fileSenderIntegrationService.createHeadersForRequest();

        var body = fileSenderIntegrationService.createBodyForRequest(tempFile);

        try {
            return fileSenderIntegrationService.executeRequest(body, headers);
        } finally {
            fileSenderIntegrationService.removeTempFile(tempFile);
        }

    }

    public ResponseEntity<byte[]> fastConvert(MultipartFile file) {
        var headers = fileSenderIntegrationService.createHeadersForRequest();
        var body = fileSenderIntegrationService.createBodyForRequest(file);
        return fileSenderIntegrationService.executeRequest(body, headers);
    }

}
