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
import java.util.regex.PatternSyntaxException;

import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class CountLinesCommandTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private CountLinesCommand countLinesCommand;

    @BeforeEach
    void reinitialize() {
        countLinesCommand = new CountLinesCommand();
    }

    @Test
    @DisplayName("throw an exception if called with the wrong number of arguments")
    void givenWrongNumberOfArguments_thenThrowException() throws IOException {
        String input = prepareEmptyFile(tempDir).toString();
        String[] oneArg = new String[]{input};
        String[] threeArgs = new String[]{input, "1", "2",};


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> countLinesCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> countLinesCommand.execute(oneArg));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> countLinesCommand.execute(threeArgs));
    }

    @Test
    @DisplayName("throw an exception if the input file does not exist")
    void givenNonExistingInputFile_thenThrowException() {
        String input = tempDir.resolve("nonexistinginput.txt").toString();
        String[] args = new String[]{input, "2"};


        Assertions.assertThrows(FileNotFoundException.class,
                () -> countLinesCommand.execute(args));
    }

    @Test
    @DisplayName("throw an exception if an invalid pattern is used")
    void givenInvalidPattern_thenThrowException() throws IOException {
        String input = prepareEmptyFile(tempDir).toString();
        String[] args = new String[]{input, "[[[[["};


        Assertions.assertThrows(PatternSyntaxException.class,
                () -> countLinesCommand.execute(args));
    }

    @Test
    @DisplayName("normal execution")
    void normalExecution() throws Exception {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("Test1", "abc", "Test2", "def", "Test3"));
        String[] args = new String[]{input.toString(), "Test"};


        countLinesCommand.execute(args);


        assertThat(countLinesCommand.getCounter(), is(3));
    }
}
