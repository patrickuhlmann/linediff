package ch.uhlme.diff;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class OutputFolder implements Closeable {
  private final BufferedWriter bothWriter;
  private final BufferedWriter onlyFirstWriter;
  private final BufferedWriter onlySecondWriter;

  /**
   * Outputfolder for the LineDiff command. It will create the folder as well as three writers for
   * the result files.
   *
   * @param folder mustn't exist.
   * @throws IOException if an error occurs during the creation of the folder or with the writers of
   *     the result files.
   * @throws IllegalArgumentException if the folder already exists
   */
  public OutputFolder(Path folder) throws IOException {
    Objects.requireNonNull(folder);

    if (Files.exists(folder)) {
      throw new IllegalArgumentException("The output folder mustn't exist");
    }

    Files.createDirectories(folder);

    Path first = Paths.get(folder.toAbsolutePath() + File.separator + "first_only.txt");
    Path second = Paths.get(folder.toAbsolutePath() + File.separator + "second_only.txt");
    Path both = Paths.get(folder.toAbsolutePath() + File.separator + "both.txt");

    onlyFirstWriter = Files.newBufferedWriter(first, StandardCharsets.UTF_8);
    onlySecondWriter = Files.newBufferedWriter(second, StandardCharsets.UTF_8);
    bothWriter = Files.newBufferedWriter(both, StandardCharsets.UTF_8);
  }

  void addBothLine(String line) throws IOException {
    bothWriter.append(line);
    bothWriter.newLine();
  }

  void addOnlyFirstLine(String line) throws IOException {
    onlyFirstWriter.append(line);
    onlyFirstWriter.newLine();
  }

  void addOnlySecondLine(String line) throws IOException {
    onlySecondWriter.append(line);
    onlySecondWriter.newLine();
  }

  void flush() throws IOException {
    bothWriter.flush();
    onlyFirstWriter.flush();
    onlySecondWriter.flush();
  }

  @Override
  public void close() throws IOException {
    bothWriter.close();
    onlyFirstWriter.close();
    onlySecondWriter.close();
  }
}
