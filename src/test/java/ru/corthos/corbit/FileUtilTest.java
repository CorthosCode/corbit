package ru.corthos.corbit;

import org.junit.jupiter.api.Test;
import ru.corthos.corbit.txt.MetaFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilTest extends BaseFileTest {

    @Test
    void getMetaFile() throws IOException {
        Path path = Path.of("files/example.txt");
        MetaFile actual = new MetaFile("example.txt", "txt", "Hello world from TXT to PDF!");
        MetaFile expected = fileUtil.getMetaFile(path);
        assertEquals(actual, expected);
    }

    @Test
    void insertPdfPath() {
        Path path = Path.of("files/example.txt");
        Path actual = path.getParent();
        Path pdfPath = fileUtil.insertPdfPath(path, "example.txt", "txt");
        Path expected = pdfPath.getParent();
        assertEquals(actual, expected);
    }

    @Test
    void createPDF() throws FileNotFoundException {
        Path path = Path.of("files/create_pdf.pdf");
        fileUtil.createPDF(path, "Example text");
        createdFiles.add(path);
    }

}