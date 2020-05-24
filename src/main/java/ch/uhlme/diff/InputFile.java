package ch.uhlme.diff;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class InputFile implements Closeable {
  private final BufferedReader reader;
  private String previousLine = null;

  /**
   * opens a reader. The file must exist
   *
   * @param inputFile path of the file
   * @throws IOException if the file cannot be opened for read
   * @throws IllegalArgumentException if the file does not exist
   */
  public InputFile(Path inputFile) throws IOException {
    Objects.requireNonNull(inputFile);

    if (!Files.exists(inputFile)) {
      throw new IllegalArgumentException("Input file must exist");
    }

    reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8);
  }

  /**
   * Reads the next distinct line from the file. This means it will ignore duplicates. Thus if the
   * next line is the same as the previous, it will automatically ignore it and again read the next
   * line until it is different.
   *
   * @return the next distinct line from the file
   * @throws IOException if an error happens when reading the next line
   */
  String getNextUniqueLine() throws IOException {
    String nextLine = reader.readLine();
    while (nextLine != null && nextLine.equals(previousLine)) {
      nextLine = reader.readLine();
    }

    previousLine = nextLine;
    return nextLine;
  }

  @Override
  public void close() throws IOException {
    this.reader.close();
  }
}
