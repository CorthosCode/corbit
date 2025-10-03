package ru.corthos.corbit.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.corthos.corbit.service.ConverterService;

@RestController
public class FileReceiverController {

    private final ConverterService converterService;

    @Autowired
    public FileReceiverController(ConverterService converterService) {
        this.converterService = converterService;
    }

    @SneakyThrows
    @PostMapping("/upload")
    public ResponseEntity<byte[]> upload(@RequestParam("file") MultipartFile file) {
        var response = converterService.convert(file);
        return responseFor(response);
    }

    private ResponseEntity<byte[]> responseFor(ResponseEntity<byte[]> response) {
        return ResponseEntity.ok()
                /**
                 * The Unicode character [А] at code point [1,040]
                 * cannot be encoded as it is outside the permitted range of 0 to 255
                 *
                 * возникает потому, что в HTTP-заголовке Content-Disposition браузеру отдаётся имя файла,
                 * где есть кириллица (А, Б, ё и т.п.),
                 * а стандарт RFC 2616 для Content-Disposition: attachment; filename="..."
                 * допускает только ASCII (0–255).
                 */

//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tempFile.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "result.pdf" + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(response.getBody());
    }

}
