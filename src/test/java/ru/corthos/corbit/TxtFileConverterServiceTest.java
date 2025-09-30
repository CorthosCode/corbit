package ru.corthos.corbit;

import org.junit.jupiter.api.Test;
import ru.corthos.corbit.txt.TxtFileConverterService;
import ru.corthos.corbit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TxtFileConverterServiceTest extends BaseFileTest {

    @Test
    public void convertTxtToPDFTest() throws IOException {
        FileUtil fileUtil = new FileUtil();
        TxtFileConverterService service = new TxtFileConverterService(fileUtil);

        Path path = Path.of("files/example.txt");
        String actualFileName = path.getFileName().toString().replaceFirst("\\.txt$", ".pdf");

        var expected = service.convertTxtToPDF(path);
        createdFiles.add(expected);

        assertEquals(actualFileName, expected.getFileName().toString());
    }

}