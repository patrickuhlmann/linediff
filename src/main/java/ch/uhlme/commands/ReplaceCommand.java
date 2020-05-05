package ch.uhlme.commands;

import ch.uhlme.utils.Quadruple;
import com.google.common.flogger.FluentLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ReplaceCommand implements Command {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public void execute(String[] params) throws IOException {
        logger.atInfo().log("started with %s", params);

        Quadruple<Path, Path, String, String> arguments = verifyParameters(params);

        replace(arguments.getFirst(), arguments.getSecond(), arguments.getThird(), arguments.getFourth());

        logger.atInfo().log("Process finished successfully");
    }

    @Override
    public String getName() {
        return "replace";
    }

    private Quadruple<Path, Path, String, String> verifyParameters(String[] args) throws FileNotFoundException, FileAlreadyExistsException {
        if (args == null || args.length != 4) {
            throw new IllegalArgumentException("usage: replace <input> <output> <searchPattern> <replacePattern>");
        }

        Path input = Paths.get(args[0]);
        if (!Files.exists(input)) {
            throw new FileNotFoundException(String.format("the input file %s can't be found", input));
        }

        Path output = Paths.get(args[1]);
        if (Files.exists(output)) {
            throw new FileAlreadyExistsException(String.format("the output file %s mustn't exist", output));
        }

        return new Quadruple<>(input, output, args[2], args[3]);
    }

    private void replace(Path input, Path output, String searchPattern, String replacePattern) throws IOException {
        Objects.requireNonNull(searchPattern);
        Objects.requireNonNull(replacePattern);

        try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {

            String currentLine = reader.readLine();
            while (currentLine != null) {
                String replaced = currentLine.replaceAll(searchPattern, replacePattern);
                writer.write(replaced);
                writer.newLine();
                currentLine = reader.readLine();
            }
        }
    }
}
