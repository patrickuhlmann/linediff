package ch.uhlme.commands;

import ch.uhlme.BaseTest;
import ch.uhlme.matchers.FileContentIs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.List;

import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class DecodeURLCommandTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private DecodeURLCommand decodeURLCommand;

    @BeforeEach
    void reinitialize() {
        decodeURLCommand = new DecodeURLCommand();
    }

    @Test
    @DisplayName("throw an exception if called with the wrong number of arguments")
    void givenWrongNumberOfArguments_thenThrowException() throws IOException {
        Path input = prepareEmptyFile(tempDir);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> decodeURLCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> decodeURLCommand.execute(new String[]{input.toString()}));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> decodeURLCommand.execute(new String[]{input.toString(), "1", "2"}));
    }


    @Test
    @DisplayName("throw an exception if the input file doesn't exist")
    void givenNonExistingInputFile_thenThrowException() {
        Path input = tempDir.resolve("nonexistinginput.txt");


        Assertions.assertThrows(FileNotFoundException.class,
                () -> decodeURLCommand.execute(new String[]{input.toString(), "2"}));
    }

    @Test
    @DisplayName("throw exception if the output file already exists")
    void givenExistingOutputFile_thenThrowException() throws IOException {
        Path input = prepareEmptyFile(tempDir);
        Path output = prepareEmptyFile(tempDir);


        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> decodeURLCommand.execute(new String[]{input.toString(), output.toString()}));
    }

    @Test
    @DisplayName("normal execution")
    void normalExecution() throws Exception {
        Path input = prepareFileWithLines(tempDir, List.of("%C3%9Cber", "Another"));
        Path output = tempDir.resolve("output.txt");


        decodeURLCommand.execute(new String[]{input.toString(), output.toString()});


        assertThat(output, FileContentIs.fileContentIs(List.of("Über", "Another")));
    }
}
