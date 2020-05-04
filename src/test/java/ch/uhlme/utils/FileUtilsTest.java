package ch.uhlme.utils;

import ch.uhlme.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileUtilsTest extends BaseTest {
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
        createFileOrFail(filePath);

        FileUtils.deleteRecursive(filePath);

        Assertions.assertFalse(filePath.toFile().exists());
    }

    @Test
    @DisplayName("recursive deletion with folder")
    public void deleteRecursiveFolder() throws IOException {
        Path filePath = tempDir.resolve("input");
        mkdirsOrFail(filePath);

        FileUtils.deleteRecursive(filePath);

        Assertions.assertFalse(filePath.toFile().exists());
    }

    @Test
    @DisplayName("recursive deletion with structure")
    public void deleteRecursiveStructure() throws IOException {
        Path filePath = tempDir.resolve("input");

        Path secondFolder = tempDir.resolve("input/other/other");
        Path secondFile = tempDir.resolve("input/other/other/file.txt");
        mkdirsOrFail(secondFolder);
        createFileOrFail(secondFile);

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
    public void generateFileWithRandomLinesNull() {
        Assertions.assertThrows(NullPointerException.class,
                () -> FileUtils.generateFileWithRandomLines(null, 10));
    }

    @Test
    @DisplayName("throw exception if generate with negative number")
    public void generateFileWithRandomLinesNegative() {
        Path filePath = tempDir.resolve("out.txt");

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> FileUtils.generateFileWithRandomLines(filePath, -10));
    }

    @Test
    @DisplayName("regular file with extension")
    public void regularFileExtension() {
        Assertions.assertEquals("/some/file/file_1.txt",
                FileUtils.getPathWithSuffixInFilename(Paths.get("/some/file/file.txt"), "_1").toString());
    }

    @Test
    @DisplayName("file with no extension")
    public void noExtension() {
        Assertions.assertEquals("/some/file/file_1",
                FileUtils.getPathWithSuffixInFilename(Paths.get("/some/file/file"), "_1").toString());
    }

    @Test
    @DisplayName("file without name, only extension")
    public void onlyFileExtension() {
        Assertions.assertEquals("/some/file/_1.gitignore",
                FileUtils.getPathWithSuffixInFilename(Paths.get("/some/file/.gitignore"), "_1").toString());
    }

    @Test
    @DisplayName("wrong parameters for path with suffix")
    public void wrongParametersPathWithSuffix() {
        Assertions.assertThrows(NullPointerException.class, () ->
                FileUtils.getPathWithSuffixInFilename(null, "_1"));

        Assertions.assertThrows(NullPointerException.class, () ->
                FileUtils.getPathWithSuffixInFilename(null, null));

        Assertions.assertThrows(NullPointerException.class, () ->
                FileUtils.getPathWithSuffixInFilename(Paths.get("bla"), null));
    }
}
