package ch.uhlme;

import ch.uhlme.commands.*;
import ch.uhlme.utils.LogUtils;

import java.io.IOException;
import java.util.Arrays;

public class Application {
    static {
        try {
            LogUtils.initalizeLogging("logging.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("No command specified. Available commands: linediff, externalsort, split, decodeurl, countlines, removelines and replace");
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
            case "decodeurl":
                DecodeURLCommand decodeURL = new DecodeURLCommand();
                decodeURL.run(args);
                break;
            case "countlines":
                CountLinesCommand countLinesCommand = new CountLinesCommand();
                countLinesCommand.run(args);
                break;
            case "removelines":
                RemoveLinesCommand removeLines = new RemoveLinesCommand();
                removeLines.run(args);
                break;
            default:
                throw new IllegalArgumentException(String.format("Specified command %s not found", command));
        }
    }
}
