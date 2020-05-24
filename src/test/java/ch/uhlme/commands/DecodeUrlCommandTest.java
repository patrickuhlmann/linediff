package ch.uhlme.commands;

import static ch.uhlme.preparation.PrepareFile.prepareEmptyFile;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;

import ch.uhlme.BaseTest;
import ch.uhlme.matchers.FileContentIs;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class DecodeUrlCommandTest extends BaseTest {
  @SuppressWarnings("unused")
  @TempDir
  Path tempDir;

  private DecodeUrlCommand decodeUrlCommand;

  @BeforeEach
  void reinitialize() {
    decodeUrlCommand = new DecodeUrlCommand();
  }

  @Test
  @DisplayName("throw an exception if called with the wrong number of arguments")
  void givenWrongNumberOfArguments_thenThrowException() throws IOException {
    String input = prepareEmptyFile(tempDir).toString();
    String[] oneArg = new String[] {input};
    String[] threeArgs = new String[] {input, "1", "2"};

    Assertions.assertThrows(IllegalArgumentException.class, () -> decodeUrlCommand.execute(null));

    Assertions.assertThrows(IllegalArgumentException.class, () -> decodeUrlCommand.execute(oneArg));

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> decodeUrlCommand.execute(threeArgs));
  }

  @Test
  @DisplayName("throw an exception if the input file doesn't exist")
  void givenNonExistingInputFile_thenThrowException() {
    String input = tempDir.resolve("nonexistinginput.txt").toString();
    String[] args = new String[] {input, "2"};

    Assertions.assertThrows(FileNotFoundException.class, () -> decodeUrlCommand.execute(args));
  }

  @Test
  @DisplayName("throw exception if the output file already exists")
  void givenExistingOutputFile_thenThrowException() throws IOException {
    String input = prepareEmptyFile(tempDir).toString();
    String output = prepareEmptyFile(tempDir).toString();
    String[] args = new String[] {input, output};

    Assertions.assertThrows(FileAlreadyExistsException.class, () -> decodeUrlCommand.execute(args));
  }

  @Test
  @DisplayName("normal execution")
  void normalExecution() throws Exception {
    Path input = prepareFileWithLines(tempDir, List.of("%C3%9Cber", "Another"));
    Path output = tempDir.resolve("output.txt");
    String[] args = new String[] {input.toString(), output.toString()};

    decodeUrlCommand.execute(args);

    assertThat(output, FileContentIs.fileContentIs(List.of("Über", "Another")));
  }
}
