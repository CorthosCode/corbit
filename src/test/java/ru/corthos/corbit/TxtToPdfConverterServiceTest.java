package ru.corthos.corbit;

import org.junit.jupiter.api.Test;
import ru.corthos.corbit.txt.TxtToPdfConverterService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TxtToPdfConverterServiceTest extends BaseFileTest {

    @Test
    public void convertTxtToPDFTest() throws IOException {
        TxtToPdfConverterService service = new TxtToPdfConverterService(fileUtil);
        var expected = service.convertTxtToPDF(constants.pathOfExampleFile);
        createdFiles.add(expected);
        assertEquals(constants.fileNameWithExtensionOfExamplePDF, expected.getFileName().toString());
    }

}