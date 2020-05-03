package ch.uhlme.commands;

import ch.uhlme.sorting.ExternalSort;
import ch.uhlme.utils.Tuple;
import com.google.common.flogger.FluentLogger;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExternalSortCommand {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public void run(String[] args) throws Exception {
        logger.atInfo().log("started with %s", args);

        Tuple<Path, Path> paths = verifyParameters(args);

        ExternalSort sort = new ExternalSort();
        sort.sort(paths.first, paths.second);

        logger.atInfo().log("Process finished successfully");
    }

    private Tuple<Path, Path> verifyParameters(String[] args) throws FileNotFoundException {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException("usage: externalsort <input> <output>");
        }

        Path input = Paths.get(args[0]);
        if (!input.toFile().exists()) {
            throw new FileNotFoundException(String.format("the input file %s can't be found", input));
        }

        Path output = Paths.get(args[1]);
        if (output.toFile().exists()) {
            throw new IllegalArgumentException(String.format("the output file %s mustn't exist", input));
        }

        return new Tuple<>(input, output);
    }
}
