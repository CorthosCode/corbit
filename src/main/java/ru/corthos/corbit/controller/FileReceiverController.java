package ru.corthos.corbit.controller;

import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.corthos.corbit.txt.TxtToPdfConverterService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class FileReceiverController {

    private final TxtToPdfConverterService txtToPdfConverterService;
    private final Tika tika;

    @Autowired
    public FileReceiverController(TxtToPdfConverterService txtToPdfConverterService) {
        this.txtToPdfConverterService = txtToPdfConverterService;
        this.tika = new Tika();
    }

    @SneakyThrows
    @PostMapping("/upload")
    public ResponseEntity<byte[]> upload(@RequestParam("file") MultipartFile file) {

        // Проверяем MimeType файла
        validateFile(file);

        // Сохраняем во временный файл
        Path tempTxt = createTempFile(file);

        // Конвертируем в PDF
        Path pdfPath = txtToPdfConverterService.convertTxtToPDF(tempTxt);

        // Читаем PDF как массив байтов
        byte[] pdfBytes = Files.readAllBytes(pdfPath);

        // Возвращаем пользователю PDF сразу для скачивания
        return responseForPDF(pdfPath, pdfBytes);

    }

    private void validateFile(MultipartFile file) throws IOException {
        String mimeType = tika.detect(file.getInputStream());

        if (!mimeType.equals(MimeTypes.PLAIN_TEXT)) {
            throw new IllegalArgumentException("Неверный формат файла: " + mimeType);
        }
    }

    private Path createTempFile(MultipartFile file) throws IOException {
        Path tempTxt = Files.createTempFile("upload-", ".txt");
        file.transferTo(tempTxt);
        return tempTxt;
    }

    private ResponseEntity<byte[]> responseForPDF(Path pdfPath, byte[] pdfBytes) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfPath.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
