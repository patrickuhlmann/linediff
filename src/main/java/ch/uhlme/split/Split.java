package ch.uhlme.split;

import ch.uhlme.utils.FileUtils;
import com.google.common.flogger.FluentLogger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class Split {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final Path inputFile;
  private final int maxLinesPerFile;
  private int fileNumber = 1;
  private BufferedWriter writer = null;
  private int linesInCurrentFile = 0;

  /**
   * Defines the file to split and the number of lines after which a split occurs
   *
   * <p>In order to execute the split, the split function must be called.
   *
   * @param input path to the file to be split
   * @param maxLinesPerFile the max number of lines per file after which a split occurs
   */
  public Split(Path input, int maxLinesPerFile) {
    Objects.requireNonNull(input);

    this.inputFile = input;
    this.maxLinesPerFile = maxLinesPerFile;
  }

  /**
   * Execute the split. As a result it will generate files with the same name, suffixed with a
   * number. e. g. input.txt will be split to input_1.txt, input_2.txt and input_3.txt
   *
   * @throws IOException if an error occurs when reading the input file or writing any of the output
   *     files
   */
  public void split() throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
      String currentLine = reader.readLine();
      while (currentLine != null) {
        processLine(currentLine);
        currentLine = reader.readLine();
      }
    } finally {
      if (writer != null) {
        writer.close();
      }
    }

    logger.atFine().log("split with input %s, lines %d", inputFile, maxLinesPerFile);
  }

  private void processLine(String currentLine) throws IOException {
    if (writer == null) {
      startNextOutputFile();
    }

    writer.write(currentLine);
    linesInCurrentFile++;

    if (linesInCurrentFile == maxLinesPerFile) {
      linesInCurrentFile = 0;
      startNextOutputFile();
      return;
    }

    writer.newLine();
  }

  private void startNextOutputFile() throws IOException {
    Path output = getOutputFile();
    logger.atInfo().log("started output file %s", output);
    if (writer != null) {
      writer.close();
    }
    writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8);
  }

  private Path getOutputFile() throws FileAlreadyExistsException {
    Path outputFile = FileUtils.getPathWithSuffixInFilename(inputFile, "_" + fileNumber);
    if (Files.exists(outputFile)) {
      throw new FileAlreadyExistsException(
          String.format("the output file %s already exists", outputFile));
    }
    fileNumber++;
    return outputFile;
  }
}
