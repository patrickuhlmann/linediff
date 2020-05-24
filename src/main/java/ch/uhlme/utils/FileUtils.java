package ch.uhlme.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.SplittableRandom;

public class FileUtils {
    private static final SplittableRandom random = new SplittableRandom();

    private FileUtils() {
    }

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
