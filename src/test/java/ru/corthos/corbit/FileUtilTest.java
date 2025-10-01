package ru.corthos.corbit;

import org.junit.jupiter.api.Test;
import ru.corthos.corbit.util.MetaFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilTest extends BaseFileTest {

    @Test
    void getMetaFile() throws IOException {
        MetaFile actual = new MetaFile(
                constants.fileNameOfExample,
                constants.extensionOfExample,
                constants.contentOfExample,
                null
        );
        MetaFile expected = fileUtil.getMetaFile(constants.pathOfExampleTxtFile);
        assertEquals(actual, expected);
    }

    @Test
    void createFilePathWithNewExtension() {
        Path actual = constants.pathOfExampleTxtFile.getParent();
        Path pdfPath = fileUtil.createFilePathWithNewExtension(
                constants.pathOfExampleTxtFile,
                constants.fileNameOfExample,
                constants.extensionOfExample
        );
        Path expected = pdfPath.getParent();
        assertEquals(actual, expected);
    }

    @Test
    void createPDF() throws FileNotFoundException {
        Path path = Path.of(constants.pathOfCreatedExamplePdfFile.toString());
        fileUtil.createPDF(path, constants.contentOfExample);
        createdFiles.add(path);
    }

}