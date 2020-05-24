package ch.uhlme.preparation;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class PrepareFile {
  public static Path prepareEmptyFile(Path folder) throws IOException {
    UUID uuid = UUID.randomUUID();
    return prepareEmptyFile(folder, uuid.toString() + ".txt");
  }

  public static Path prepareEmptyFile(Path folder, String filename) throws IOException {
    Path input = folder.resolve(filename);
    if (!input.toFile().createNewFile()) {
      Assertions.fail(String.format("Unable to create file %s", input));
    }

    return input;
  }

  public static Path prepareFileWithLines(Path folder, String filename, List<String> lines)
          throws IOException {
    Path path = prepareEmptyFile(folder, filename);
    Files.write(path, lines, StandardCharsets.UTF_8);
    return path;
  }

  public static Path prepareFileWithLines(Path folder, List<String> lines) throws IOException {
    Path path = prepareEmptyFile(folder);
    Files.write(path, lines, StandardCharsets.UTF_8);
    return path;
  }
}
