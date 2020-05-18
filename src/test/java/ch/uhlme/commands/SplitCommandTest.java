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
import java.util.Arrays;

import static ch.uhlme.matchers.FileContentIs.fileContentIs;
import static ch.uhlme.matchers.FileNotExists.fileNotExists;
import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class SplitCommandTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private SplitCommand splitCommand;

    @BeforeEach
    void reinitialize() {
        splitCommand = new SplitCommand();
    }

    @Test
    @DisplayName("throw exception if called with the wrong number of arguments")
    void givenWrongNumberOfArguments_thenThrowException() {
        String[] oneArg = new String[]{"1"};
        String[] threeArgs = new String[]{"1", "2", "3"};

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> splitCommand.execute(null));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> splitCommand.execute(oneArg));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> splitCommand.execute(threeArgs));
    }

    @Test
    @DisplayName("throw exception if the input file doesn't exist")
    void givenInputFileNonExisting_thenThrowException() {
        Assertions.assertThrows(FileNotFoundException.class,
                () -> splitCommand.execute(new String[]{"inputnonexisting.txt", "10"}));
    }

    @Test
    @DisplayName("throw exception if line is not a positive integer")
    void givenLineNotPositiveInteger_thenThrowException() throws IOException {
        String input = prepareEmptyFile(tempDir).toString();
        String[] argWithString = new String[]{input, "some"};
        String[] argWithMinus = new String[]{input, "-20"};
        String[] argZero = new String[]{input, "0"};


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> splitCommand.execute(argWithString));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> splitCommand.execute(argWithMinus));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> splitCommand.execute(argZero));
    }

    @Test
    @DisplayName("throw exception if the output file already exists")
    void givenOutputFileExist_thenThrowException() throws IOException {
        Path input = prepareFileWithLines(tempDir, "outputnotexist.txt", Arrays.asList("a", "b", "c", "d", "e"));
        prepareEmptyFile(tempDir, "outputnotexist_1.txt");
        Path inputLater = prepareFileWithLines(tempDir, "outputlater.txt", Arrays.asList("a", "b", "c", "d", "e"));
        prepareEmptyFile(tempDir, "outputlater_3.txt");
        String[] argFirst = new String[]{input.toString(), "20"};
        String[] argLater = new String[]{inputLater.toString(), "2"};


        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> splitCommand.execute(argFirst));

        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> splitCommand.execute(argLater));
    }

    @Test
    @DisplayName("less lines than we split")
    void lessLinesThanWeSplit() throws IOException {
        Path input = prepareFileWithLines(tempDir, "lesslines.txt", Arrays.asList("d", "a", "f"));
        Path output = tempDir.resolve("lesslines_1.txt");
        Path output2 = tempDir.resolve("lesslines_2.txt");
        String[] args = new String[]{input.toString(), "5"};


        splitCommand.execute(args);


        assertThat(output, fileContentIs(Arrays.asList("d", "a", "f")));
        assertThat(output2, fileNotExists());
    }

    @Test
    @DisplayName("regular execution")
    void regularExecutionWithSorting() throws IOException {
        Path input = prepareFileWithLines(tempDir, "regular.txt", Arrays.asList("a", "b", "c", "d", "e"));
        Path output1 = tempDir.resolve("regular_1.txt");
        Path output2 = tempDir.resolve("regular_2.txt");
        Path output3 = tempDir.resolve("regular_3.txt");
        Path output4 = tempDir.resolve("regular_4.txt");
        String[] args = new String[]{input.toString(), "2"};


        splitCommand.execute(args);


        assertThat(output1, fileContentIs(Arrays.asList("a", "b")));
        assertThat(output2, fileContentIs(Arrays.asList("c", "d")));
        assertThat(output3, FileContentIs.fileContentIs("e"));
        assertThat(output4, fileNotExists());
    }
}
