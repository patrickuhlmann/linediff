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
    String[] oneArg = new String[]{"1"};
    String[] fourArgs = new String[]{"1", "2", "3", "4"};

    Assertions.assertThrows(IllegalArgumentException.class, () -> diffCommand.execute(null));

    Assertions.assertThrows(IllegalArgumentException.class, () -> diffCommand.execute(oneArg));

    Assertions.assertThrows(IllegalArgumentException.class, () -> diffCommand.execute(fourArgs));
  }

  @Test
  @DisplayName("throw an exception if the input file doesn't exist")
  void givenInputFilesNotExist_thenThrowException() throws IOException {
    String input = prepareEmptyFile(tempDir).toString();
    String[] argFirstDoesNotExist = new String[]{"first.txt", "second.txt", "output.txt"};
    String[] argSecondDoesNotExist = new String[]{input, "second.txt", "output.txt"};

    Assertions.assertThrows(
            NoSuchFileException.class, () -> diffCommand.execute(argFirstDoesNotExist));

    Assertions.assertThrows(
            NoSuchFileException.class, () -> diffCommand.execute(argSecondDoesNotExist));
  }

  @Test
  @DisplayName("throw an exception if the output file already exists")
  void givenOutputFileExist_thenThrowException() throws IOException {
    String firstInput = prepareEmptyFile(tempDir).toString();
    String secondInput = prepareEmptyFile(tempDir).toString();
    Path outputFolder = tempDir.resolve("outputExists");
    Path output = tempDir.resolve("outputExists/both.txt");
    mkdirsOrFail(outputFolder);
    createFileOrFail(output);
    String[] args = new String[]{firstInput, secondInput, outputFolder.toString()};

    Assertions.assertThrows(IllegalArgumentException.class, () -> diffCommand.execute(args));
  }

  @Test
  @DisplayName("throw an exception if a file is unsorted")
  void givenUnsortedFile_thenThrowsException() throws IOException {
    String unsortedInput = prepareFileWithLines(tempDir, Arrays.asList("g", "b", "c")).toString();
    String sortedInput = prepareFileWithLines(tempDir, Arrays.asList("a", "b", "c")).toString();
    String output = tempDir.resolve("outputUnsorted").toString();
    String[] argFirstUnsorted = new String[]{unsortedInput, sortedInput, output};
    String[] argSecondUnsorted = new String[]{sortedInput, unsortedInput, output};

    Assertions.assertThrows(
            IllegalArgumentException.class, () -> diffCommand.execute(argFirstUnsorted));

    Assertions.assertThrows(
            IllegalArgumentException.class, () -> diffCommand.execute(argSecondUnsorted));
  }

  @Test
  @DisplayName("throw an exception if used with the same input file")
  void givenSameInputFile_thenThrowException() throws IOException {
    String input = prepareFileWithLines(tempDir, Arrays.asList("g", "b", "c")).toString();
    String output = tempDir.resolve("outputSame").toString();
    String[] argSameInput = new String[]{input, input, output};

    Assertions.assertThrows(
            IllegalArgumentException.class, () -> diffCommand.execute(argSameInput));
  }

  @Test
  @DisplayName("normal execution")
  void normalExecution() throws IOException {
    String firstInput = prepareFileWithLines(tempDir, Arrays.asList("b", "c", "g")).toString();
    String secondInput = prepareFileWithLines(tempDir, Arrays.asList("d", "f", "g")).toString();
    Path output = tempDir.resolve("output");
    Path outputBoth = Paths.get(output.toString(), "both.txt");
    Path outputFirst = Paths.get(output.toString(), "first_only.txt");
    Path outputSecond = Paths.get(output.toString(), "second_only.txt");
    String[] args = new String[]{firstInput, secondInput, output.toString()};

    diffCommand.execute(args);

    assertThat(outputBoth, fileContentIs(Collections.singletonList("g")));
    assertThat(outputFirst, fileContentIs(Arrays.asList("b", "c")));
    assertThat(outputSecond, fileContentIs(Arrays.asList("d", "f")));
  }
}
