package ru.corthos.corbit;

import org.junit.jupiter.api.Test;
import ru.corthos.corbit.txt.TxtFileConverterService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TxtFileConverterServiceTest extends BaseFileTest {

    @Test
    public void convertTxtToPDFTest() throws IOException {
        TxtFileConverterService service = new TxtFileConverterService(fileUtil);
        var expected = service.convertTxtToPDF(constants.pathOfExampleFile);
        createdFiles.add(expected);
        assertEquals(constants.fileNameWithExtensionOfExamplePDF, expected.getFileName().toString());
    }

}