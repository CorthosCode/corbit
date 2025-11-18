package ru.corthos.backend;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class MultipartFileUtil {
    public static MockMultipartFile getMultipartFile() {
        byte[] bytes = "content".getBytes();
        return getMultipartFileFromContent(bytes);
    }

    public static MockMultipartFile getMultipartFileFromContent(byte[] bytes) {
        return new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                bytes
        );
    }
}
