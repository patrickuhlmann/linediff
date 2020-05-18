package ch.uhlme.commands;

import ch.uhlme.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.PatternSyntaxException;

import static ch.uhlme.matchers.FileContentIs.fileContentIs;
import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class RemoveLinesCommandTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private RemoveLinesCommand removeLinesCommand;

    @BeforeEach
    public void reinitialize() {
        removeLinesCommand = new RemoveLinesCommand();
    }

    @Test
    @DisplayName("throw exception if called with the wrong number of arguments")
    void givenWrongNumberOfArguments_thenThrowException() throws IOException {
        String input = prepareEmptyFile(tempDir).toString();
        String[] twoArgs = new String[]{input, "2"};
        String[] fiveArgs = new String[]{input, "2", "3", "4", "5"};


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> removeLinesCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> removeLinesCommand.execute(twoArgs));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> removeLinesCommand.execute(fiveArgs));
    }


    @Test
    @DisplayName("throw exception if the input file doesn't exist")
    void givenNonExistingInputFile_thenThrowException() {
        String input = tempDir.resolve("nonexistinginput.txt").toString();
        String[] args = new String[]{input, "2", "3"};


        Assertions.assertThrows(FileNotFoundException.class,
                () -> removeLinesCommand.execute(args));
    }

    @Test
    @DisplayName("throw exception if the output file already exists")
    void outputFileMustntExist() throws IOException {
        String input = prepareEmptyFile(tempDir).toString();
        String output = prepareEmptyFile(tempDir).toString();
        String[] args = new String[]{input, output, "3"};


        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> removeLinesCommand.execute(args));
    }

    @Test
    @DisplayName("throw exception if the pattern is invalid")
    void givenInvalidPattern_thenThrowException() throws IOException {
        String input = prepareEmptyFile(tempDir).toString();
        Path output = tempDir.resolve("output.txt");
        String[] args = new String[]{input, output.toString(), "[[[[["};


        Assertions.assertThrows(PatternSyntaxException.class,
                () -> removeLinesCommand.execute(args));
    }

    @Test
    @DisplayName("normal execution")
    void normalExecution() throws Exception {
        String input = prepareFileWithLines(tempDir, Arrays.asList("line1", "test", "line2", "", "line3")).toString();
        Path output = tempDir.resolve("output.txt");
        String[] args = new String[]{input, output.toString(), "test"};

        removeLinesCommand.execute(args);


        assertThat(output, fileContentIs(Arrays.asList("line1", "line2", "", "line3")));
    }
}
