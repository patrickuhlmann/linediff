package ch.uhlme.preparation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;

public class PrepareFile {
  /**
   * Create a textfile with a random name containg the given lines.
   *
   * @param folder folder in which to create the file
   * @return path to the created file
   * @throws IOException if an error occurs during writing
   */
  public static Path prepareEmptyFile(Path folder) throws IOException {
    UUID uuid = UUID.randomUUID();
    return prepareEmptyFile(folder, uuid.toString() + ".txt");
  }

  /**
   * Create an empty file in the given folder.
   *
   * @param folder folder in which to create the file
   * @param filename name of the file
   * @return path of the created file
   * @throws IOException if the creation fails
   */
  public static Path prepareEmptyFile(Path folder, String filename) throws IOException {
    Path input = folder.resolve(filename);
    if (!input.toFile().createNewFile()) {
      Assertions.fail(String.format("Unable to create file %s", input));
    }

    return input;
  }

  /**
   * Create a file containing the given lines in a given folder.
   *
   * @param folder folder in which to create the file
   * @param filename name of the file
   * @param lines lines to write
   * @return path of the created file
   * @throws IOException if an error occurs when writing
   */
  public static Path prepareFileWithLines(Path folder, String filename, List<String> lines)
      throws IOException {
    Path path = prepareEmptyFile(folder, filename);
    Files.write(path, lines, StandardCharsets.UTF_8);
    return path;
  }

  /**
   * Create a textfile with a random name containg the given lines.
   *
   * @param folder folder in which to create the file
   * @param lines lines to write in the file
   * @return path to the created file
   * @throws IOException if an error occurs during writing
   */
  public static Path prepareFileWithLines(Path folder, List<String> lines) throws IOException {
    Path path = prepareEmptyFile(folder);
    Files.write(path, lines, StandardCharsets.UTF_8);
    return path;
  }
}
