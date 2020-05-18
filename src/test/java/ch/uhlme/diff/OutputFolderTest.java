package ch.uhlme.diff;

import ch.uhlme.Application;
import ch.uhlme.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class OutputFolderTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("exception if path is null")
    void givenNullPath_thenThrowException() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new OutputFolder(null));
    }

    @Test
    @DisplayName("throw exception if an output file already exists")
    void givenOutputExists_thenThrowException() throws IOException {
        String firstInput = prepareEmptyFile(tempDir).toString();
        String secondInput = prepareEmptyFile(tempDir).toString();
        Path outputFolder = tempDir.resolve("output");
        Path output = tempDir.resolve("output/both.txt");
        mkdirsOrFail(outputFolder);
        createFileOrFail(output);
        String[] args = new String[]{"linediff", firstInput, secondInput, outputFolder.toString()};


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Application.main(args));
        deleteFileOrFail(output);

        output = tempDir.resolve("output/first_only.txt");
        createFileOrFail(output);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Application.main(args)
        );
        deleteFileOrFail(output);

        output = tempDir.resolve("output/second_only.txt");
        createFileOrFail(output);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Application.main(args)
        );
    }

    @Test
    @DisplayName("normal execution")
    void normalExecution() {
        Path filePath = tempDir.resolve("output");


        Assertions.assertDoesNotThrow(() -> {
            new OutputFolder(filePath);
        });
    }
}
