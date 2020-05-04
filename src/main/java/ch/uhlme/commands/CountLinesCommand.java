package ch.uhlme.commands;

import ch.uhlme.utils.Tuple;
import com.google.common.flogger.FluentLogger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class CountLinesCommand {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private int counter;

    public void run(String[] args) throws IOException {
        logger.atInfo().log("started with %s", args);

        Tuple<Path, Pattern> arguments = verifyParameters(args);

        count(arguments.getFirst(), arguments.getSecond());

        logger.atInfo().log("Process finished successfully");
    }

    private Tuple<Path, Pattern> verifyParameters(String[] args) throws FileNotFoundException {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException("usage: countlines <input> <pattern>");
        }

        Path input = Paths.get(args[0]);
        if (!Files.exists(input)) {
            throw new FileNotFoundException(String.format("the input file %s can't be found", input));
        }

        Pattern pattern = Pattern.compile(args[1]);

        return new Tuple<>(input, pattern);
    }

    private void count(Path input, Pattern pattern) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8)) {
            String currentLine = reader.readLine();
            while (currentLine != null) {
                Matcher m = pattern.matcher(currentLine);
                if (m.find()) {
                    counter++;
                }
                currentLine = reader.readLine();
            }
        }

        System.out.println(String.format("Expression was found %d times", counter));
    }

    public int getCounter() {
        return counter;
    }
}
