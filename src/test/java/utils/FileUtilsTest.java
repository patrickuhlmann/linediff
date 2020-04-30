package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileUtilsTest {
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("lines are sorted")
    public void linesSorted() throws IOException {
        List<String> sortedLines = Arrays.asList("abc", "def");

        Path filePath = tempDir.resolve("input1.txt");
        Files.write(filePath, sortedLines, StandardCharsets.UTF_8);

        Assertions.assertTrue(FileUtils.areLinesInFileSorted(filePath));
    }

    @Test
    @DisplayName("lines are unsorted")
    public void linesUnsorted() throws IOException {
        List<String> unsortedLines = Arrays.asList("def", "abc");

        Path filePath = tempDir.resolve("input1.txt");
        Files.write(filePath, unsortedLines, StandardCharsets.UTF_8);

        Assertions.assertFalse(FileUtils.areLinesInFileSorted(filePath));
    }

    @Test
    @DisplayName("recursive deletion with file")
    public void deleteRecursiveFile() throws IOException {
        Path filePath = tempDir.resolve("input1.txt");
        filePath.toFile().createNewFile();

        FileUtils.deleteRecursive(filePath);

        Assertions.assertFalse(filePath.toFile().exists());
    }

    @Test
    @DisplayName("recursive deletion with folder")
    public void deleteRecursiveFolder() throws IOException {
        Path filePath = tempDir.resolve("input");
        filePath.toFile().mkdir();

        FileUtils.deleteRecursive(filePath);

        Assertions.assertFalse(filePath.toFile().exists());
    }

    @Test
    @DisplayName("recursive deletion with structure")
    public void deleteRecursiveStructure() throws IOException {
        Path filePath = tempDir.resolve("input");

        Path secondFile = tempDir.resolve("input/other/other/file.txt");
        secondFile.toFile().mkdirs();
        secondFile.toFile().createNewFile();

        FileUtils.deleteRecursive(filePath);

        Assertions.assertFalse(filePath.toFile().exists());
    }

    @Test
    @DisplayName("generate file with random lines")
    public void generateFileWithRandomLines() throws IOException {
        Path filePath = tempDir.resolve("out.txt");

        FileUtils.generateFileWithRandomLines(filePath, 10);

        List<String> lines = Files.readAllLines(filePath);
        Assertions.assertEquals(10, lines.size());
    }

    @Test
    @DisplayName("throw exception if generate file is null")
    public void generateFileWithRandomLinesNull() throws IOException {
        Path filePath = tempDir.resolve("out.txt");

        Assertions.assertThrows(NullPointerException.class, () -> {
            FileUtils.generateFileWithRandomLines(null, 10);
        });
    }

    @Test
    @DisplayName("throw exception if generate with negative number")
    public void generateFileWithRandomLinesNegative() throws IOException {
        Path filePath = tempDir.resolve("out.txt");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.generateFileWithRandomLines(filePath, -10);
        });
    }
}
