package ch.uhlme.diff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class OutputFolder implements AutoCloseable {
    private final BufferedWriter bothWriter;
    private final BufferedWriter onlyFirstWriter;
    private final BufferedWriter onlySecondWriter;

    public OutputFolder(Path folder) throws IOException {
        Objects.requireNonNull(folder);

        Files.createDirectories(folder);

        Path first = Paths.get(folder.toAbsolutePath() + File.separator + "first_only.txt");
        Path second = Paths.get(folder.toAbsolutePath() + File.separator + "second_only.txt");
        Path both = Paths.get(folder.toAbsolutePath() + File.separator + "both.txt");

        if (first.toFile().exists() || second.toFile().exists() || both.toFile().exists()) {
            throw new IllegalArgumentException(String.format("Files in the output folder %s mustn't exist", folder));
        }

        onlyFirstWriter = new BufferedWriter(new FileWriter(first.toFile()));
        onlySecondWriter = new BufferedWriter(new FileWriter(second.toFile()));
        bothWriter = new BufferedWriter(new FileWriter(both.toFile()));
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
