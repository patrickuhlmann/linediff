package ch.uhlme.commands;

import ch.uhlme.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static ch.uhlme.matchers.FileContentIs.fileContentIs;
import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class LineDiffCommandTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private LineDiffCommand diffCommand;

    @BeforeEach
    void reinitialize() {
        diffCommand = new LineDiffCommand();
    }

    @Test
    @DisplayName("throw an exception if called with the wrong number of arguments")
    void givenWrongNumberOfArguments_thenThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{"1"}));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{"1", "2", "3", "4"}));
    }

    @Test
    @DisplayName("throw an exception if the input file doesn't exist")
    void givenInputFilesNotExist_thenThrowException() throws IOException {
        Path input = prepareEmptyFile(tempDir);


        Assertions.assertThrows(NoSuchFileException.class,
                () -> diffCommand.execute(new String[]{"first.txt", "second.txt", "output.txt"}));

        Assertions.assertThrows(NoSuchFileException.class,
                () -> diffCommand.execute(new String[]{input.toString(), "second.txt", "output.txt"}));
    }

    @Test
    @DisplayName("throw an exception if the output file already exists")
    void givenOutputFileExist_thenThrowException() throws IOException {
        Path firstInput = prepareEmptyFile(tempDir);
        Path secondInput = prepareEmptyFile(tempDir);
        Path outputFolder = tempDir.resolve("outputExists");
        Path output = tempDir.resolve("outputExists/both.txt");
        mkdirsOrFail(outputFolder);
        createFileOrFail(output);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{firstInput.toString(), secondInput.toString(), outputFolder.toString()}));
    }

    @Test
    @DisplayName("throw an exception if a file is unsorted")
    void givenUnsortedFile_thenThrowsException() throws IOException {
        Path unsortedInput = prepareFileWithLines(tempDir, Arrays.asList("g", "b", "c"));
        Path sortedInput = prepareFileWithLines(tempDir, Arrays.asList("a", "b", "c"));
        Path output = tempDir.resolve("outputUnsorted");


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{unsortedInput.toString(), sortedInput.toString(), output.toString()}));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{sortedInput.toString(), unsortedInput.toString(), output.toString()}));
    }

    @Test
    @DisplayName("throw an exception if used with the same input file")
    void givenSameInputFile_thenThrowException() throws IOException {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("g", "b", "c"));
        Path output = tempDir.resolve("outputSame");


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{input.toString(), input.toString(), output.toString()}));
    }

    @Test
    @DisplayName("normal execution")
    void normalExecution() throws IOException {
        Path firstInput = prepareFileWithLines(tempDir, Arrays.asList("b", "c", "g"));
        Path secondInput = prepareFileWithLines(tempDir, Arrays.asList("d", "f", "g"));
        Path output = tempDir.resolve("output");
        Path outputBoth = Paths.get(output.toString(), "both.txt");
        Path outputFirst = Paths.get(output.toString(), "first_only.txt");
        Path outputSecond = Paths.get(output.toString(), "second_only.txt");


        diffCommand.execute(new String[]{firstInput.toString(), secondInput.toString(), output.toString()});


        assertThat(outputBoth, fileContentIs(Collections.singletonList("g")));
        assertThat(outputFirst, fileContentIs(Arrays.asList("b", "c")));
        assertThat(outputSecond, fileContentIs(Arrays.asList("d", "f")));
    }
}
