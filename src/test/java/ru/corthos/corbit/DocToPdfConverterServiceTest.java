package ru.corthos.corbit;

import org.junit.jupiter.api.Test;
import ru.corthos.corbit.docx.DocToPdfConverterService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocToPdfConverterServiceTest extends BaseFileTest{

    @Test
    void convertDocToPDF() throws IOException {
        DocToPdfConverterService service = new DocToPdfConverterService(fileUtil);
        var expected = service.convertDocToPDF(constants.pathOfExampleDocFile);
        createdFiles.add(expected);
        assertEquals(constants.fileNameWithExtensionOfExamplePDF, expected.getFileName().toString());
    }
}