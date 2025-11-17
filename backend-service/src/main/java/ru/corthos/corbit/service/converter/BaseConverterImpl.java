package ru.corthos.corbit.service.converter;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BaseConverterImpl implements ConvertingToPDF {

    private final RestTemplate restTemplate;

    protected BaseConverterImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Path createTempFile(MultipartFile file) {
        try {
            var tempFile = Files.createTempFile("temp-", file.getOriginalFilename());
            file.transferTo(tempFile);
            IO.println(String.format("TempFile - %s - created", tempFile.getFileName()));
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Невозможно создать временный файл из " + file.getName());
        }
    }

    @Override
    public void removeTempFile(Path tempFile) {
        try {
            var deleted = Files.deleteIfExists(tempFile);
            IO.println(String.format("TempFile - %s - removed - %s", tempFile.getFileName(), deleted));
        } catch (IOException exception) {
            throw new RuntimeException("Невозможно удалить временный файл из " + tempFile.getFileName());
        }
    }

    protected HttpHeaders createHeadersForRequest() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

    protected MultiValueMap<String, Object> createBodyForRequest(Path tempFile, String key) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(key, new FileSystemResource(tempFile));
        return body;
    }

    protected MultiValueMap<String, Object> createBodyForRequest(MultipartFile file, String key) {
        try {
            ByteArrayResource resource = getByteArrayResource(file);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add(key, resource);
            return body;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось получить ByteArrayResource из " + file.getName());
        }
    }

    protected ResponseEntity<byte[]> executeRequest(MultiValueMap<String, Object> body, HttpHeaders headers, String path) {
        return restTemplate.exchange(
                path,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                byte[].class
        );
    }

    private ByteArrayResource getByteArrayResource(MultipartFile file) throws IOException {
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
    }

}
