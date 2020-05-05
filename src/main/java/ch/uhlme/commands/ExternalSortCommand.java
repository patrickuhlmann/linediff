package ch.uhlme.commands;

import ch.uhlme.sorting.ExternalSort;
import ch.uhlme.utils.Tuple;
import com.google.common.flogger.FluentLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExternalSortCommand implements Command {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public void execute(String[] params) throws IOException {
        logger.atInfo().log("started with %s", params);

        Tuple<Path, Path> paths = verifyParameters(params);

        ExternalSort sort = new ExternalSort();
        sort.sort(paths.getFirst(), paths.getSecond());

        logger.atInfo().log("Process finished successfully");
    }

    @Override
    public String getName() {
        return "externalsort";
    }

    private Tuple<Path, Path> verifyParameters(String[] args) throws FileNotFoundException {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException("usage: externalsort <input> <output>");
        }

        Path input = Paths.get(args[0]);
        if (!Files.exists(input)) {
            throw new FileNotFoundException(String.format("the input file %s can't be found", input));
        }

        Path output = Paths.get(args[1]);
        if (Files.exists(output)) {
            throw new IllegalArgumentException(String.format("the output file %s mustn't exist", input));
        }

        return new Tuple<>(input, output);
    }
}
