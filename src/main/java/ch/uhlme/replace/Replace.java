package ch.uhlme.replace;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

public class Replace {
    public void replace(Path input, Path output, String searchPattern, String replacePattern) throws IOException {
        Objects.requireNonNull(input);
        Objects.requireNonNull(output);
        Objects.requireNonNull(searchPattern);
        Objects.requireNonNull(replacePattern);

        try (BufferedReader reader = new BufferedReader(new FileReader(input.toFile()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(output.toFile()))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String replaced = currentLine.replaceAll(searchPattern, replacePattern);
                writer.write(replaced);
                writer.newLine();
            }
        }
    }
}
