package ru.corthos.corbit.util;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openpdf.text.Document;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileUtil {

    public MetaFile getMetaFile(Path filePath) throws IOException {
        var fileNameWithExtension = filePath.getFileName().toString();
        var fileName = fileNameWithExtension.substring(0, fileNameWithExtension.indexOf("."));
        var fileExtension = fileNameWithExtension.substring(fileNameWithExtension.indexOf(".") + 1);
        var fileContent = Files.readAllLines(filePath);
        return new MetaFile(fileName, fileExtension, fileContent, null);
    }

    public MetaFile getMetaFileForDOCX(Path filePath) throws IOException {
        var fileNameWithExtension = filePath.getFileName().toString();
        var fileName = fileNameWithExtension.substring(0, fileNameWithExtension.indexOf("."));
        var fileExtension = fileNameWithExtension.substring(fileNameWithExtension.indexOf(".") + 1);

        List<String> fileContent = new ArrayList<>();
        XWPFDocument doc;

        try (InputStream fis = Files.newInputStream(filePath)) {
            doc = new XWPFDocument(fis);

            for (XWPFParagraph para : doc.getParagraphs()) {
                fileContent.add(para.getText());
            }
        }

        return new MetaFile(fileName, fileExtension, fileContent, doc);
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

    public void convertToPDF(Path pdfPath, MetaFile metaFile) throws IOException {
        PdfOptions pdfOptions = PdfOptions.create();
        OutputStream out = Files.newOutputStream(pdfPath);
        PdfConverter.getInstance().convert(metaFile.doc(), out, pdfOptions);
        metaFile.doc().close();
        out.close();
    }
}
