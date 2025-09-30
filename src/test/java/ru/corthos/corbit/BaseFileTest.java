package ru.corthos.corbit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import ru.corthos.corbit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BaseFileTest {

    protected static FileUtil fileUtil;
    protected static List<Path> createdFiles;
    protected static Constants constants;

    @BeforeAll
    public static void setup() {
        fileUtil = new FileUtil();
        createdFiles = new ArrayList<>();
        constants = new Constants();
    }

    @AfterAll
    public static void cleanUp() {
        createdFiles.forEach(p -> {
            try {
                boolean result = fileUtil.removeFileFrom(p);
                IO.println(String.format("File removed - %s - %s ", result, p.getFileName().toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
