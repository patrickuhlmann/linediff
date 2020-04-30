import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

public class OutputFolderTest extends BaseTest {
    @Test
    @DisplayName("exception with empty path")
    public void exceptionWithEmptyFile() {
        Assertions.assertThrows(NullPointerException.class, () -> new OutputFolder(null));
    }

    @Test
    @DisplayName("invalid an output file already exists")
    public void invvalidIfExists(@TempDir Path tempDir) throws IOException {
        Path firstInput = tempDir.resolve("input1.txt");
        firstInput.toFile().createNewFile();

        Path secondInput = tempDir.resolve("input2.txt");
        secondInput.toFile().createNewFile();

        Path outputFolder = tempDir.resolve("output");
        Path output = tempDir.resolve("output/both.txt");
        output.toFile().mkdirs();
        output.toFile().createNewFile();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Application.main(new String[] { firstInput.toString(), secondInput.toString(), outputFolder.toString() });
        });

        output.toFile().delete();

        output = tempDir.resolve("output/first_only.txt");
        output.toFile().createNewFile();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Application.main(new String[] { firstInput.toString(), secondInput.toString(), outputFolder.toString() });
        });

        output.toFile().delete();

        output = tempDir.resolve("output/second_only.txt");
        output.toFile().createNewFile();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Application.main(new String[] { firstInput.toString(), secondInput.toString(), outputFolder.toString() });
        });
    }

    @Test
    @DisplayName("file is valid if it doesn't exist")
    public void validIfEmpty(@TempDir Path tempDir) {
        Path filePath = tempDir.resolve("output");

        Assertions.assertDoesNotThrow(() -> {
            new OutputFolder(filePath);
        });
    }
}
