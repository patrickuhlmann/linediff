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
        String[] oneArg = new String[]{"1"};
        String[] threeArgs = new String[]{"1", "2", "3"};


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> sortCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> sortCommand.execute(oneArg));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> sortCommand.execute(threeArgs));
    }

    @Test
    @DisplayName("throw an exception if the input file doesn't exist")
    void givenNonExistingInputFile_thenThrowException() {
        String[] args = new String[]{"input.txt", "output.txt"};


        Assertions.assertThrows(FileNotFoundException.class,
                () -> sortCommand.execute(args));
    }

    @Test
    @DisplayName("throw an exception if the output file already exists")
    void givenExistingOutputFile_thenThrowException() throws IOException {
        String input = prepareEmptyFile(tempDir).toString();
        String output = prepareEmptyFile(tempDir).toString();
        String[] args = new String[]{input, output};


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> sortCommand.execute(args));
    }

    @Test
    @DisplayName("normal execution")
    void normalExecution() throws IOException {
        String input = prepareFileWithLines(tempDir, Arrays.asList("d", "a", "f")).toString();
        Path output = tempDir.resolve("output.txt");
        String[] args = new String[]{input, output.toString()};


        sortCommand.execute(args);


        assertThat(output, fileContentIs(Arrays.asList("a", "d", "f")));
    }
}
