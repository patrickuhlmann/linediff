package ch.uhlme.commands;

import ch.uhlme.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static ch.uhlme.matchers.FileContentIs.fileContentIs;
import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class ExternalSortCommandTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private ExternalSortCommand sortCommand;

    @BeforeEach
    void reinitialize() {
        sortCommand = new ExternalSortCommand();
    }

    @Test
    @DisplayName("throw an exception if called with the wrong number of arguments")
    void givenWrongNumberOfArguments_thenThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> sortCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> sortCommand.execute(new String[]{"1"}));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> sortCommand.execute(new String[]{"1", "2", "3"}));
    }

    @Test
    @DisplayName("throw an exception if the input file doesn't exist")
    void givenNonExistingInputFile_thenThrowException() {
        Assertions.assertThrows(FileNotFoundException.class,
                () -> sortCommand.execute(new String[]{"input.txt", "output.txt"}));
    }

    @Test
    @DisplayName("throw an exception if the output file already exists")
    void givenExistingOutputFile_thenThrowException() throws IOException {
        Path input = prepareEmptyFile(tempDir);
        Path output = prepareEmptyFile(tempDir);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> sortCommand.execute(new String[]{input.toString(), output.toString()}));
    }

    @Test
    @DisplayName("normal execution")
    void normalExecution() throws IOException {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("d", "a", "f"));
        Path output = tempDir.resolve("output.txt");


        sortCommand.execute(new String[]{input.toString(), output.toString()});


        assertThat(output, fileContentIs(Arrays.asList("a", "d", "f")));
    }
}
