package ru.corthos.corbit;

import java.nio.file.Path;
import java.util.List;

public class Constants {
    protected final Path pathOfExampleTxtFile = Path.of("files/example.txt");
    protected final Path pathOfExampleDocFile = Path.of("files/example.docx");
    protected final Path pathOfCreatedExamplePdfFile = Path.of("files/create_example.pdf");
    protected final String fileNameWithExtensionOfExampleTXT = "example.txt";
    protected final String fileNameWithExtensionOfExamplePDF = "example.pdf";
    protected final String fileNameOfExample = "example";
    protected final String extensionOfExample = "txt";
    protected final List<String> contentOfExample = List.of("Hello world from TXT to PDF!");
}
