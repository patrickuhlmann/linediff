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

import static ch.uhlme.matchers.FileContentIs.fileContentIs;
import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class ReplaceCommandTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private ReplaceCommand replaceCommand;

    @BeforeEach
    void reinitialize() {
        replaceCommand = new ReplaceCommand();
    }

    @Test
    @DisplayName("throw exception if called with the wrong number of arguments")
    void givenWrongNumberOfArguments_thenThrowExeption() throws IOException {
        Path input = prepareEmptyFile(tempDir);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> replaceCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> replaceCommand.execute(new String[]{input.toString(), "2", "3"}));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> replaceCommand.execute(new String[]{input.toString(), "2", "3", "4", "5"}));
    }


    @Test
    @DisplayName("throw exception if the input file doesn't exist")
    void givenInputFileNonExisting_thenThrowException() {
        Path input = tempDir.resolve("inputnonexisting.txt");

        Assertions.assertThrows(FileNotFoundException.class,
                () -> replaceCommand.execute(new String[]{input.toString(), "2", "3", "4"}));
    }

    @Test
    @DisplayName("throw exception if the output file already exists")
    void outputFileMustntExist() throws IOException {
        Path input = prepareEmptyFile(tempDir);
        Path output = prepareEmptyFile(tempDir);


        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> replaceCommand.execute(new String[]{input.toString(), output.toString(), "3", "4"}));
    }

    @Test
    @DisplayName("regular execution")
    void regularExecution() throws Exception {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("test 123 test", "main 123 main"));
        Path output = tempDir.resolve("output.txt");


        replaceCommand.execute(new String[]{input.toString(), output.toString(), "\\s[0-9]*\\s", " "});


        assertThat(output, fileContentIs(Arrays.asList("test test", "main main")));
    }
}
