package ch.uhlme;

import ch.uhlme.commands.ExternalSortCommand;
import ch.uhlme.commands.LineDiffCommand;
import ch.uhlme.commands.ReplaceCommand;
import ch.uhlme.commands.SplitCommand;
import ch.uhlme.utils.LogUtils;
import com.google.common.flogger.FluentLogger;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Application {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    static {
        try {
            LogUtils.initalizeLogging("logging.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("No command specified. Available commands: linediff, externalsort, split and replace");
        }

        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        switch (command) {
            case "linediff":
                LineDiffCommand lineDiff = new LineDiffCommand();
                lineDiff.run(args);
                break;
            case "externalsort":
                ExternalSortCommand sort = new ExternalSortCommand();
                sort.run(args);
                break;
            case "split":
                SplitCommand split = new SplitCommand();
                split.run(args);
                break;
            case "replace":
                ReplaceCommand replace = new ReplaceCommand();
                replace.run(args);
                break;
            default:
                throw new IllegalArgumentException(String.format("Specified command %s not found", command));
        }
    }
}
