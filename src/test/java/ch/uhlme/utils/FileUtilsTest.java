package ch.uhlme.utils;

import ch.uhlme.BaseTest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static ch.uhlme.matchers.FileExists.fileExists;
import static ch.uhlme.matchers.FileNotExists.fileNotExists;
import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class FileUtilsTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;

    Path createOutputFolder() throws IOException {
        Path filePath = tempDir.resolve("output");
        return Files.createDirectory(filePath);
    }

    @Test
    @DisplayName("lines are sorted")
    void givenFile_thenSorted() throws IOException {
        Path inputFile = prepareFileWithLines(tempDir, Arrays.asList("abc", "def"));


        Assertions.assertTrue(FileUtils.areLinesInFileSorted(inputFile));
    }

    @Test
    @DisplayName("lines are unsorted")
    void givenFile_thenUnsorted() throws IOException {
        Path filePath = prepareFileWithLines(tempDir, Arrays.asList("def", "dee"));


        Assertions.assertFalse(FileUtils.areLinesInFileSorted(filePath));
    }

    @Test
    @DisplayName("delete a single file")
    void givenFile_thenDelete() throws IOException {
        Path filePath = prepareEmptyFile(tempDir);
        assertThat(filePath, fileExists());


        FileUtils.deleteRecursive(filePath);


        assertThat(filePath, fileNotExists());
    }

    @Test
    @DisplayName("delete a single folder")
    void givenFolder_thenDelete() throws IOException {
        Path filePath = createOutputFolder();
        assertThat(filePath, fileExists());


        FileUtils.deleteRecursive(filePath);


        assertThat(filePath, fileNotExists());
    }

    @Test
    @DisplayName("delete a folder structure with files")
    void givenFolderStructure_thenDelete() throws IOException {
        Path filePath = tempDir.resolve("input");
        Path secondFolder = tempDir.resolve("input/other/other");
        Path secondFile = tempDir.resolve("input/other/other/file.txt");
        mkdirsOrFail(secondFolder);
        createFileOrFail(secondFile);
        assertThat(filePath, fileExists());


        FileUtils.deleteRecursive(filePath);


        assertThat(filePath, fileNotExists());
    }

    @Test
    @DisplayName("generate file with random lines")
    void givenGenerate_thenFileExists() throws IOException {
        Path filePath = tempDir.resolve("out.txt");

        FileUtils.generateFileWithRandomLines(filePath, 10);
        List<String> lines = Files.readAllLines(filePath);
        FileUtils.generateFileWithRandomLines(filePath, 1);
        List<String> lines2 = Files.readAllLines(filePath);


        assertThat(lines, hasSize(10));
        assertThat(lines2, hasSize(1));
        assertThat(lines2.get(0).length(), is(100));
    }

    @Test
    @DisplayName("throw exception if generate file is null")
    void givenGenerateWithNullFile_thenThrowException() {
        Assertions.assertThrows(NullPointerException.class,
                () -> FileUtils.generateFileWithRandomLines(null, 10));
    }

    @Test
    @DisplayName("throw exception if generate with negative number")
    void givenGenerateWithNegativeNumber_thenThrowException() {
        Path filePath = tempDir.resolve("out.txt");

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> FileUtils.generateFileWithRandomLines(filePath, -10));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> FileUtils.generateFileWithRandomLines(filePath, -1));
    }

    @SuppressFBWarnings(
            value = "DMI_HARDCODED_ABSOLUTE_FILENAME",
            justification = "OK for test")
    @Test
    @DisplayName("add suffix in regular filename")
    void givenRegularFile_thenAddSuffix() {
        String original = "/some/file/file.txt";
        String fileWithSuffix = FileUtils.getPathWithSuffixInFilename(Paths.get(original), "_1").toString();


        assertThat("/some/file/file_1.txt", is(fileWithSuffix));
    }

    @SuppressFBWarnings(
            value = "DMI_HARDCODED_ABSOLUTE_FILENAME",
            justification = "OK for test")
    @Test
    @DisplayName("add suffix in file without extension")
    void givenFilewithoutExtension_thenAddSuffix() {
        String original = "/some/file/file";
        String fileWithSuffix = FileUtils.getPathWithSuffixInFilename(Paths.get(original), "_1").toString();


        assertThat("/some/file/file_1", is(fileWithSuffix));
    }

    @SuppressFBWarnings(
            value = "DMI_HARDCODED_ABSOLUTE_FILENAME",
            justification = "OK for test")
    @Test
    @DisplayName("add suffix if file has no name, only extension")
    void givenFileWithNoName_thenAddSuffix() {
        String original = "/some/file/.gitignore";
        String fileWithSuffix = FileUtils.getPathWithSuffixInFilename(Paths.get(original), "_1").toString();


        assertThat("/some/file/_1.gitignore", is(fileWithSuffix));
    }

    @Test
    @DisplayName("throw exception if wrong parameters specified for pathWithSuffix")
    void wrongParametersPathWithSuffix() {
        Assertions.assertThrows(NullPointerException.class,
                () -> FileUtils.getPathWithSuffixInFilename(null, "_1"));

        Assertions.assertThrows(NullPointerException.class,
                () -> FileUtils.getPathWithSuffixInFilename(null, null));

        Path bla = Paths.get("bla");
        Assertions.assertThrows(NullPointerException.class,
                () -> FileUtils.getPathWithSuffixInFilename(bla, null));
    }
}
