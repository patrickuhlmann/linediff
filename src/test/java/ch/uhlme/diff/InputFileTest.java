package ch.uhlme.diff;

import ch.uhlme.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class InputFileTest extends BaseTest {
    private final static String INPUT_FILENAME = "input.txt";
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("exception with empty file")
    public void exceptionWithEmptyFile() {
        Assertions.assertThrows(NullPointerException.class, () -> new InputFile(null));
    }

    @Test
    @DisplayName("file is invalid if not exists")
    public void invvalidIfNotExists() {
        Path filePath = tempDir.resolve(INPUT_FILENAME);  // NOPMD

        Assertions.assertThrows(NoSuchFileException.class, () -> new InputFile(filePath));
    }

    @Test
    @DisplayName("file is valid if empty")
    public void validIfEmpty() throws IOException {
        Path filePath = tempDir.resolve(INPUT_FILENAME);
        createFileOrFail(filePath);

        Assertions.assertDoesNotThrow(() -> {
            new InputFile(filePath);
        });
    }

    @Test
    @DisplayName("file is valid if sorted")
    public void validIfSorted() throws IOException {
        List<String> sortedLines = Arrays.asList("abc", "def");

        Path filePath = tempDir.resolve(INPUT_FILENAME);
        Files.write(filePath, sortedLines, StandardCharsets.UTF_8);

        Assertions.assertDoesNotThrow(() -> {
            new InputFile(filePath);
        });
    }

    @Test
    @DisplayName("file is valid if not sorted")
    public void invalidIfNotSorted() throws IOException {
        List<String> unsortedLines = Arrays.asList("def", "abc");

        Path filePath = tempDir.resolve(INPUT_FILENAME);
        Files.write(filePath, unsortedLines, StandardCharsets.UTF_8);

        Assertions.assertDoesNotThrow(() -> {
            new InputFile(filePath);
        });
    }
}
