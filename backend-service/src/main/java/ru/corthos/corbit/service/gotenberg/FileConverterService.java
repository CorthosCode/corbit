package ru.corthos.corbit.service.gotenberg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.corthos.corbit.service.converter.BaseConverterIntegrationImpl;

@Slf4j
@Profile("gotenberg")
@Service
public class FileConverterService extends BaseConverterIntegrationImpl {

    public FileConverterService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public ResponseEntity<byte[]> convert(MultipartFile file) {
        log.info("Начало конвертации файла: originalFilename='{}', size={} bytes", file.getOriginalFilename(), file.getSize());
        var path = "/forms/libreoffice/convert";
        var key = "files";
        return super.executeRequest(file, path, key);
    }

}
