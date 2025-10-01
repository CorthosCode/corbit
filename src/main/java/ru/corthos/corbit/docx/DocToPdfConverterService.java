package ru.corthos.corbit.docx;

import lombok.SneakyThrows;
import org.openpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.corthos.corbit.util.FileUtil;

import java.nio.file.Files;
import java.nio.file.Path;

import static ru.corthos.corbit.util.Extensions.DOC;
import static ru.corthos.corbit.util.Extensions.PDF;

@Service
public class DocToPdfConverterService {

    private final FileUtil fileUtil;

    @Autowired
    public DocToPdfConverterService(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @SneakyThrows
    public Path convertDocToPDF(Path filePath) throws DocumentException {

        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Файл не найден: " + filePath);
        }

        var metaFile = fileUtil.getMetaFileForDOCX(filePath);

        if (!metaFile.extension().contains(DOC)) {
            throw new IllegalArgumentException("Файл не с расширением: .TXT");
        }

        Path pdfPath = fileUtil.createFilePathWithNewExtension(filePath, metaFile.name(), PDF);

        fileUtil.convertToPDF(pdfPath, metaFile);

        return pdfPath;
    }

}
