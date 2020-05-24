package ch.uhlme.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.SplittableRandom;

public class FileUtils {
  private static final SplittableRandom random = new SplittableRandom();

  private FileUtils() {}

  /**
   * Delete a path including all subdirectories and files recursively. It also works if called on
   * just a file. Note: if an exception occurs, some files/folders might already be deleted.
   *
   * @param path path to delete
   * @throws IOException if any file/folder cannot be deleted.
   */
  public static void deleteRecursive(Path path) throws IOException {
    Files.walkFileTree(
        path,
        new SimpleFileVisitor<>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
          }
        });
  }

  /**
   * Checks if the lines in a file are sorted (using the default comparator).
   *
   * @param path file to check
   * @return true if sorted, false otherwise
   * @throws IOException if an error occurs while reading the file.
   */
  public static boolean areLinesInFileSorted(Path path) throws IOException {
    Objects.requireNonNull(path);

    try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      String previousLine = ""; // NOPMD
      String currentLine = br.readLine();
      while (currentLine != null) {
        if (previousLine.compareTo(currentLine) > 0) {
          return false;
        }

        previousLine = currentLine; // NOPMD
        currentLine = br.readLine();
      }
    }

    return true;
  }

  /**
   * Generates a file containing random lines. Each line consists of 100 random characters between
   * a-z
   *
   * @param path file to generate
   * @param lines number of lines to generate
   * @throws IOException if an error occurs during writing the file
   * @throws IllegalArgumentException if the number of lines is not positive
   */
  public static void generateFileWithRandomLines(Path path, long lines) throws IOException {
    Objects.requireNonNull(path);

    if (lines <= 0) {
      throw new IllegalArgumentException("lines must be positive");
    }

    try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      for (int i = 0; i < lines; i++) {
        bw.write(randomLine());
        bw.newLine();
      }
      bw.flush();
    }
  }

  private static String randomLine() {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 100;

    return random
        .ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  /**
   * Takes a path and returns the same path with a suffix inserted in the filename, respecting
   * fileextension. It handles files with extensions, files that have only an extension as well as
   * files without an extension.
   *
   * <p>Examples (with suffix _1:
   * <li>input.txt - input_1.txt
   * <li>.gitignore - _1.gitignore
   * <li>input - input_1
   *
   * @param path path of the file
   * @param suffix suffix to insert
   * @return path with the file containing the suffix
   */
  public static Path getPathWithSuffixInFilename(Path path, String suffix) {
    Objects.requireNonNull(path);
    Objects.requireNonNull(suffix);

    int fileExtensionPosition = path.toString().lastIndexOf('.');

    String filename;
    if (fileExtensionPosition > -1) {
      filename = path.toString().substring(0, fileExtensionPosition);
      filename += suffix;
      filename += path.toString().substring(fileExtensionPosition);
    } else {
      filename = path.toString() + suffix;
    }
    return Paths.get(filename);
  }
}
