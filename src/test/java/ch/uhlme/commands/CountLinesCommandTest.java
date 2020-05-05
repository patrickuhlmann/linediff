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
        Path input = prepareEmptyFile(tempDir);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> countLinesCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> countLinesCommand.execute(new String[]{input.toString()}));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> countLinesCommand.execute(new String[]{input.toString(), "1", "2",}));
    }

    @Test
    @DisplayName("throw an exception if the input file does not exist")
    void givenNonExistingInputFile_thenThrowException() {
        Path input = tempDir.resolve("nonexistinginput.txt");


        Assertions.assertThrows(FileNotFoundException.class,
                () -> countLinesCommand.execute(new String[]{input.toString(), "2"}));
    }

    @Test
    @DisplayName("throw an exception if an invalid pattern is used")
    void givenInvalidPattern_thenThrowException() throws IOException {
        Path input = prepareEmptyFile(tempDir);

        Assertions.assertThrows(PatternSyntaxException.class,
                () -> countLinesCommand.execute(new String[]{input.toString(), "[[[[["}));
    }

    @Test
    @DisplayName("normal execution")
    void normalExecution() throws Exception {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("Test1", "abc", "Test2", "def", "Test3"));


        countLinesCommand.execute(new String[]{input.toString(), "Test"});


        assertThat(countLinesCommand.getCounter(), is(3));
    }
}
