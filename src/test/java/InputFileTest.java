import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class InputFileTest extends BaseTest {
    @Test
    @DisplayName("exception with empty file")
    public void exceptionWithEmptyFile() {
        Assertions.assertThrows(NullPointerException.class, () -> new InputFile(null));
    }

    @Test
    @DisplayName("file is invalid if not exists")
    public void invvalidIfNotExists(@TempDir Path tempDir) {
        Path filePath = tempDir.resolve("input1.txt");

        Assertions.assertThrows(FileNotFoundException.class, () -> new InputFile(filePath));
    }

    @Test
    @DisplayName("file is valid if empty")
    public void validIfEmpty(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("input1.txt");
        filePath.toFile().createNewFile();

        Assertions.assertDoesNotThrow(() -> {
            new InputFile(filePath);
        });
    }

    @Test
    @DisplayName("file is valid if sorted")
    public void validIfSorted(@TempDir Path tempDir) throws IOException {
        List<String> sortedLines = Arrays.asList("abc", "def");

        Path filePath = tempDir.resolve("input1.txt");
        Files.write(filePath, sortedLines, StandardCharsets.UTF_8);

        Assertions.assertDoesNotThrow(() -> {
            new InputFile(filePath);
        });
    }

    @Test
    @DisplayName("file is valid if not sorted")
    public void invalidIfNotSorted(@TempDir Path tempDir) throws IOException {
        List<String> unsortedLines = Arrays.asList("def", "abc");

        Path filePath = tempDir.resolve("input1.txt");
        Files.write(filePath, unsortedLines, StandardCharsets.UTF_8);

        Assertions.assertDoesNotThrow(() -> {
            new InputFile(filePath);
        });
    }
}
