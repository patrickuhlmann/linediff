package ch.uhlme.diff;

import ch.uhlme.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class InputFileTest extends BaseTest {
    private final static String INPUT_FILENAME = "input.txt";
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("throw exception if called with null")
    void givenNull_thenThrowsException() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new InputFile(null));
    }

    @Test
    @DisplayName("throw exception if the file does not exist")
    void givenFileNotExisting_thenThrowException() {
        Path filePath = tempDir.resolve(INPUT_FILENAME);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new InputFile(filePath));
    }

    @Test
    @DisplayName("normal execution")
    void normalExecution() throws IOException {
        Path filePath = prepareEmptyFile(tempDir);


        Assertions.assertDoesNotThrow(() -> {
            new InputFile(filePath);
        });
    }
}
