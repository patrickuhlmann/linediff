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
        Path input = prepareEmptyFile(tempDir);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> removeLinesCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> removeLinesCommand.execute(new String[]{input.toString(), "2"}));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> removeLinesCommand.execute(new String[]{input.toString(), "2", "3", "4", "5"}));
    }


    @Test
    @DisplayName("throw exception if the input file doesn't exist")
    void givenNonExistingInputFile_thenThrowException() {
        Path input = tempDir.resolve("nonexistinginput.txt");

        Assertions.assertThrows(FileNotFoundException.class,
                () -> removeLinesCommand.execute(new String[]{input.toString(), "2", "3"}));
    }

    @Test
    @DisplayName("throw exception if the output file already exists")
    void outputFileMustntExist() throws IOException {
        Path input = prepareEmptyFile(tempDir);
        Path output = prepareEmptyFile(tempDir);


        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> removeLinesCommand.execute(new String[]{input.toString(), output.toString(), "3"}));
    }

    @Test
    @DisplayName("throw exception if the pattern is invalid")
    void givenInvalidPattern_thenThrowException() throws IOException {
        Path input = prepareEmptyFile(tempDir);
        Path output = tempDir.resolve("output.txt");


        Assertions.assertThrows(PatternSyntaxException.class,
                () -> removeLinesCommand.execute(new String[]{input.toString(), output.toString(), "[[[[["}));
    }

    @Test
    @DisplayName("normal execution")
    void normalExecution() throws Exception {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("line1", "test", "line2", "", "line3"));
        Path output = tempDir.resolve("output.txt");


        removeLinesCommand.execute(new String[]{input.toString(), output.toString(), "test"});


        assertThat(output, fileContentIs(Arrays.asList("line1", "line2", "", "line3")));
    }
}
