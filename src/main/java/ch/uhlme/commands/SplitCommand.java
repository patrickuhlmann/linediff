package ch.uhlme.commands;

import ch.uhlme.split.Split;
import ch.uhlme.utils.Tuple;
import com.google.common.flogger.FluentLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SplitCommand implements Command {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public void execute(String[] params) throws IOException {
        logger.atInfo().log("started with %s", params);

        Tuple<Path, Integer> arguments = verifyParameters(params);

        Split split = new Split(arguments.getFirst(), arguments.getSecond());
        split.split();

        logger.atInfo().log("Process finished successfully");
    }

    @Override
    public String getName() {
        return "split";
    }

    private Tuple<Path, Integer> verifyParameters(String[] args) throws FileNotFoundException {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException("usage: split <input> <numberOfLinesPerFile>");
        }

        Path input = Paths.get(args[0]);
        if (!Files.exists(input)) {
            throw new FileNotFoundException(String.format("the input file %s can't be found", input));
        }

        int lines;
        try {
            lines = Integer.parseInt(args[1]); // NOPMD
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("The number of lines must be a positive number");
        }

        if (lines <= 0) {
            throw new IllegalArgumentException("The number of lines must be a positive number");
        }

        return new Tuple<>(input, lines);
    }
}
