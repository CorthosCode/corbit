package ru.corthos.corbit.txt;

import org.openpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.corthos.corbit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TxtFileConverterService {

    private final FileUtil fileUtil;

    @Autowired
    public TxtFileConverterService(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public Path convertTxtToPDF(Path filePath) throws IOException, DocumentException {

        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Файл не найден: " + filePath);
        }

        var metaFile = fileUtil.getMetaFile(filePath);
        Path pdfPath = fileUtil.insertPdfPath(filePath, metaFile.name(), metaFile.extension());
        fileUtil.createPDF(pdfPath, metaFile.content());

        return pdfPath;
    }

}
