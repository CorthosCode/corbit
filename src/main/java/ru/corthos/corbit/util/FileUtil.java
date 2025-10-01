package ru.corthos.corbit.util;

import org.openpdf.text.Document;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class FileUtil {

    public MetaFile getMetaFile(Path filePath) throws IOException {
        var fileNameWithExtension = filePath.getFileName().toString();
        var fileName = fileNameWithExtension.substring(0, fileNameWithExtension.indexOf("."));
        var fileExtension = fileNameWithExtension.substring(fileNameWithExtension.indexOf(".") + 1);
        var fileContent = Files.readAllLines(filePath);
        return new MetaFile(fileName, fileExtension, fileContent);
    }

    public Path createFilePathWithNewExtension(Path filePath, String fileName, String fileExtension) {
        var pdfFileName = String.format("%s.%s", fileName, fileExtension);
        return filePath.getParent().resolve(pdfFileName);
    }

    public void createPDF(Path pdfPath, List<String> content) throws FileNotFoundException {
        var document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfPath.toFile()));
        document.open();
        for (String paragraph : content) {
            document.add(new Paragraph(paragraph));
        }
        document.close();
    }

    public boolean removeFileFrom(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

}
