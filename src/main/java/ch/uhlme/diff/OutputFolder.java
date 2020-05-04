package ch.uhlme.diff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class OutputFolder implements AutoCloseable {
    private final transient BufferedWriter bothWriter;
    private final transient BufferedWriter onlyFirstWriter;
    private final transient BufferedWriter onlySecondWriter;

    public OutputFolder(Path folder) throws IOException {
        Objects.requireNonNull(folder);

        Files.createDirectories(folder);

        Path first = Paths.get(folder.toAbsolutePath() + File.separator + "first_only.txt");
        Path second = Paths.get(folder.toAbsolutePath() + File.separator + "second_only.txt");
        Path both = Paths.get(folder.toAbsolutePath() + File.separator + "both.txt");

        if (Files.exists(first) || Files.exists(second) || Files.exists(both)) {
            throw new IllegalArgumentException(String.format("Files in the output folder %s mustn't exist", folder));
        }

        onlyFirstWriter = Files.newBufferedWriter(first, StandardCharsets.UTF_8);
        onlySecondWriter = Files.newBufferedWriter(second, StandardCharsets.UTF_8);
        bothWriter = Files.newBufferedWriter(both, StandardCharsets.UTF_8);
    }

    public void addBothLine(String line) throws IOException {
        bothWriter.append(line);
        bothWriter.newLine();
    }

    public void addOnlyFirstLine(String line) throws IOException {
        onlyFirstWriter.append(line);
        onlyFirstWriter.newLine();
    }

    public void addOnlySecondLine(String line) throws IOException {
        onlySecondWriter.append(line);
        onlySecondWriter.newLine();
    }

    public void flush() throws IOException {
        bothWriter.flush();
        onlyFirstWriter.flush();
        onlySecondWriter.flush();
    }

    @Override
    public void close() throws Exception {
        if (bothWriter != null) {
            bothWriter.close();
        }
        if (onlyFirstWriter != null) {
            onlyFirstWriter.close();
        }
        if (onlySecondWriter != null) {
            onlySecondWriter.close();
        }
    }
}
