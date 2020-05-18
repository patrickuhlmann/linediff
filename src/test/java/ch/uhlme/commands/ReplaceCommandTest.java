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
        String input = prepareEmptyFile(tempDir).toString();
        String[] threeArgs = new String[]{input, "2", "3"};
        String[] fiveArgs = new String[]{input, "2", "3", "4", "5"};


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> replaceCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> replaceCommand.execute(threeArgs));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> replaceCommand.execute(fiveArgs));
    }


    @Test
    @DisplayName("throw exception if the input file doesn't exist")
    void givenInputFileNonExisting_thenThrowException() {
        String input = tempDir.resolve("inputnonexisting.txt").toString();
        String[] args = new String[]{input, "2", "3", "4"};


        Assertions.assertThrows(FileNotFoundException.class,
                () -> replaceCommand.execute(args));
    }

    @Test
    @DisplayName("throw exception if the output file already exists")
    void outputFileMustntExist() throws IOException {
        String input = prepareEmptyFile(tempDir).toString();
        String output = prepareEmptyFile(tempDir).toString();
        String[] args = new String[]{input, output, "3", "4"};


        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> replaceCommand.execute(args));
    }

    @Test
    @DisplayName("regular execution")
    void regularExecution() throws Exception {
        String input = prepareFileWithLines(tempDir, Arrays.asList("test 123 test", "main 123 main")).toString();
        Path output = tempDir.resolve("output.txt");
        String[] args = new String[]{input, output.toString(), "\\s[0-9]*\\s", " "};

        replaceCommand.execute(args);


        assertThat(output, fileContentIs(Arrays.asList("test test", "main main")));
    }
}
