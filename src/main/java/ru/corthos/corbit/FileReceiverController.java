package ru.corthos.corbit;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class FileReceiverController {

    private final RestTemplate restTemplate;

    @Autowired
    public FileReceiverController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SneakyThrows
    @PostMapping("/upload")
    public ResponseEntity<byte[]> upload(@RequestParam("file") MultipartFile file) {

        Path tempFile = createTempFile(file);

        MultiValueMap<String, Object> body = createBodyForRequest(tempFile);

        HttpHeaders headers = createHeadersForRequest();

        ResponseEntity<byte[]> response = executeRequest(body, headers);

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

    private ResponseEntity<byte[]> executeRequest(MultiValueMap<String, Object> body, HttpHeaders headers) {
        return restTemplate.exchange(
                "/lool/convert-to/pdf",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                byte[].class
        );
    }

    private HttpHeaders createHeadersForRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

    private MultiValueMap<String, Object> createBodyForRequest(Path tempFile) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("data", new FileSystemResource(tempFile));
        return body;
    }

    private Path createTempFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("upload-", "-" + file.getOriginalFilename());
        file.transferTo(tempFile);
        return tempFile;
    }

}
