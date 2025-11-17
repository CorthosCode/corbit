package ru.corthos.corbit.service.jodconverter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.corthos.corbit.service.converter.BaseConverterImpl;

@Slf4j
@Profile("jodconverter")
@Service
public class FileSenderIntegrationService extends BaseConverterImpl {

    public FileSenderIntegrationService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public ResponseEntity<byte[]> convert(MultipartFile file) {
        log.info("Начало конвертации файла: originalFilename='{}', size={} bytes", file.getOriginalFilename(), file.getSize());
        var path = "/lool/convert-to/pdf";
        var key = "data";
        var headers = createHeadersForRequest();
        var body = createBodyForRequest(file, key);
        return executeRequest(body, headers, path);
    }

}
