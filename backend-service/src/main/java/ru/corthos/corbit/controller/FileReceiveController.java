package ru.corthos.corbit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.corthos.corbit.service.converter.ConverterService;

@RestController
public class FileReceiveController {

    private final ConverterService converterService;

    @Autowired
    public FileReceiveController(ConverterService converterService) {
        this.converterService = converterService;
    }

    @PostMapping("/upload")
    public ResponseEntity<byte[]> upload(@RequestParam("file") MultipartFile file) {
        var response = converterService.fastConvert(file);
        return responseFor(response);
    }

    private ResponseEntity<byte[]> responseFor(ResponseEntity<byte[]> response) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "result.pdf" + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(response.getBody());
    }

}
