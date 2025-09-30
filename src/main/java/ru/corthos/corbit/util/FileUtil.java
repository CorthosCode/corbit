package ru.corthos.corbit.util;

import org.openpdf.text.Document;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import ru.corthos.corbit.txt.MetaFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileUtil {

    public MetaFile getMetaFile(Path filePath) throws IOException {
        var fileName = filePath.getFileName().toString();
        var fileExtension = fileName.substring(fileName.indexOf(".") + 1);
        var fileContent = Files.readString(filePath);
        return new MetaFile(fileName, fileExtension, fileContent);
    }

    public Path insertPdfPath(Path filePath, String fileName, String fileExtension) {
        var pdfFileName = fileName.replaceFirst("\\." + fileExtension + "$", ".pdf");
        return filePath.getParent().resolve(pdfFileName);
    }

    public void createPDF(Path pdfPath, String content) throws FileNotFoundException {
        var document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfPath.toFile()));
        document.open();
        document.add(new Paragraph(content));
        document.close();
    }

    public boolean removeFileFrom(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

}
