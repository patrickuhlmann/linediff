package ch.uhlme.diff;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class InputFile implements AutoCloseable {
    private final BufferedReader reader;
    private String previousLine = null;

    public InputFile(Path inputFile) throws IOException {
        Objects.requireNonNull(inputFile);

        reader = new BufferedReader(new FileReader(inputFile.toFile()));
    }

    public String getNextUniqueLine() throws IOException {
        String nextLine = reader.readLine();
        while (nextLine != null && nextLine.equals(previousLine)) {
            nextLine = reader.readLine();
        }

        previousLine = nextLine;
        return nextLine;
    }

    @Override
    public void close() throws Exception {
        if (this.reader != null) {
            this.reader.close();
        }
    }
}
