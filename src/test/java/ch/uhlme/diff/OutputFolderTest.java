package ch.uhlme.diff;

import ch.uhlme.Application;
import ch.uhlme.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class OutputFolderTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("exception with empty path")
    public void exceptionWithEmptyFile() {
        Assertions.assertThrows(NullPointerException.class, () -> new OutputFolder(null));
    }

    @Test
    @DisplayName("invalid an output file already exists")
    public void invvalidIfExists() throws IOException {
        Path firstInput = tempDir.resolve("input1.txt");
        createFileOrFail(firstInput);

        Path secondInput = tempDir.resolve("input2.txt");
        createFileOrFail(secondInput);

        Path outputFolder = tempDir.resolve("output");
        Path output = tempDir.resolve("output/both.txt");
        mkdirsOrFail(outputFolder);
        createFileOrFail(output);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Application.main(new String[]{"linediff", firstInput.toString(), secondInput.toString(), outputFolder.toString()}));
        deleteFileOrFail(output);

        output = tempDir.resolve("output/first_only.txt");
        createFileOrFail(output);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Application.main(new String[]{firstInput.toString(), secondInput.toString(), outputFolder.toString()}));
        deleteFileOrFail(output);

        output = tempDir.resolve("output/second_only.txt");
        createFileOrFail(output);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Application.main(new String[]{firstInput.toString(), secondInput.toString(), outputFolder.toString()}));
    }

    @Test
    @DisplayName("file is valid if it doesn't exist")
    public void validIfEmpty() {
        Path filePath = tempDir.resolve("output");

        Assertions.assertDoesNotThrow(() -> {
            new OutputFolder(filePath);
        });
    }
}
