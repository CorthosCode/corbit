package ru.corthos.backend.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.corthos.backend.MultipartFileUtil;
import ru.corthos.backend.controller.FileReceiveController;
import ru.corthos.backend.service.converter.ConverterService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileReceiveController.class)
public class FileReceiveControllerTest {

    @MockitoBean
    private ConverterService converterService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void upload_ShouldReturnPdfResponse_WhenFileIsUploaded() throws Exception {

        var mockPdfContent = "PDF content".getBytes();

        var multipartFile = MultipartFileUtil.getMultipartFile();

        var mockResponse = ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(mockPdfContent);

        when(converterService.fastConvert(any())).thenReturn(mockResponse);

        mockMvc.perform(multipart("/upload")
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"result.pdf\""))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(mockPdfContent));
    }

    @Test
    void upload_ShouldReturn500_WhenServiceThrowsException() throws Exception {

        var multipartFile = MultipartFileUtil.getMultipartFile();

        when(converterService.fastConvert(any())).thenThrow(new RuntimeException("Conversion failed"));

        mockMvc.perform(multipart("/upload")
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void upload_ShouldReturn400_WhenFileIsMissing() throws Exception {
        mockMvc.perform(multipart("/upload")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }
}
