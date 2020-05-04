package ch.uhlme.split;

import ch.uhlme.utils.FileUtils;
import com.google.common.flogger.FluentLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Split {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private final Path inputFile;
    private final int maxLinesPerFile;
    private int fileNumber = 1;
    private BufferedWriter writer = null;
    private int linesInCurrentFile = 0;

    public Split(Path input, int maxLinesPerFile) {
        Objects.requireNonNull(input);

        this.inputFile = input;
        this.maxLinesPerFile = maxLinesPerFile;
    }

    public void split() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                processLine(currentLine);
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        logger.atFine().log("split with input %s, lines %d", inputFile, maxLinesPerFile);
    }

    private void processLine(String currentLine) throws IOException {
        if (writer == null) {
            startNextOutputFile();
        }

        writer.write(currentLine);
        linesInCurrentFile++;

        if (linesInCurrentFile == maxLinesPerFile) {
            linesInCurrentFile = 0;
            startNextOutputFile();
            return;
        }

        writer.newLine();
    }

    private void startNextOutputFile() throws IOException {
        Path output = getOutputFile();
        logger.atInfo().log("started output file %s", output);
        if (writer != null) {
            writer.close();
        }
        writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8);
    }

    private Path getOutputFile() throws FileAlreadyExistsException {
        Path outputFile = FileUtils.getPathWithSuffixInFilename(inputFile, "_" + fileNumber);
        if (Files.exists(outputFile)) {
            throw new FileAlreadyExistsException(String.format("the output file %s already exists", outputFile));
        }
        fileNumber++;
        return outputFile;
    }
}
