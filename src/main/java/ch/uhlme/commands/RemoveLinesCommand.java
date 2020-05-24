package ch.uhlme.commands;

import ch.uhlme.utils.Triple;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveLinesCommand implements Command {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public void execute(String[] params) throws IOException {
        logger.atInfo().log("started with %s", params);

        Triple<Path, Path, Pattern> arguments = verifyParameters(params);

        removeLines(arguments.getFirst(), arguments.getSecond(), arguments.getThird());

        logger.atInfo().log("Process finished successfully");
    }

    @Override
    public String getName() {
        return "removelines";
    }

    private Triple<Path, Path, Pattern> verifyParameters(String[] args)
            throws FileNotFoundException, FileAlreadyExistsException {
        if (args == null || args.length != 3) {
            throw new IllegalArgumentException("usage: removelines <input> <output> <searchPattern>");
        }

        Path input = Paths.get(args[0]);
        if (!Files.exists(input)) {
            throw new FileNotFoundException(String.format("the input file %s can't be found", input));
        }

        Path output = Paths.get(args[1]);
        if (Files.exists(output)) {
            throw new FileAlreadyExistsException(
                    String.format("the output file %s mustn't exist", output));
        }

        Pattern pattern = Pattern.compile(args[2]);

        return new Triple<>(input, output, pattern);
    }

    private void removeLines(Path input, Path output, Pattern searchPattern) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {

            String currentLine = reader.readLine();
            while (currentLine != null) {
                Matcher m = searchPattern.matcher(currentLine);
                if (!m.find()) {
                    writer.write(currentLine);
                    writer.newLine();
                }
                currentLine = reader.readLine();
            }
        }
    }
}
