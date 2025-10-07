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


    public ResponseEntity<byte[]> convert(MultipartFile file) {
        var tempFile = fileSenderIntegrationService.createTempFile(file);

        var headers = fileSenderIntegrationService.createHeadersForRequest();

        var body = fileSenderIntegrationService.createBodyForRequest(tempFile);

        var response = fileSenderIntegrationService.executeRequest(body, headers);

        fileSenderIntegrationService.removeTempFile(tempFile);

        return response;
    }

}
