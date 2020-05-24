package ch.uhlme;

import ch.uhlme.commands.*;
import ch.uhlme.utils.LogUtils;

import java.util.Arrays;
import java.util.List;

public class Application {
    private static final List<Command> commands =
            Arrays.asList(
                    new CountLinesCommand(),
                    new DecodeURLCommand(),
                    new ExternalSortCommand(),
                    new LineDiffCommand(),
                    new RemoveLinesCommand(),
                    new ReplaceCommand(),
                    new SplitCommand());

    public static void main(String[] args) throws Exception {
        LogUtils.initializeLogging("logging.properties");

        if (args == null || args.length < 1) {
            throw new IllegalArgumentException(
                    String.format("No command specified. Available commands: %n%s", getAllCommandNames()));
        }

        String commandName = args[0];
        String[] parameters = Arrays.copyOfRange(args, 1, args.length);

        Command cmd = getCommand(commandName);
        cmd.execute(parameters);
    }

    private static Command getCommand(String name) {
        for (Command c : commands) {
            if (c.getName().equals(name)) {
                return c;
            }
        }

        throw new IllegalArgumentException(String.format("Specified command %s not found", name));
    }

    private static String getAllCommandNames() {
        StringBuilder commandList = new StringBuilder();
        for (Command c : commands) {
            commandList.append("\t").append(c.getName());
        }
        return commandList.toString();
    }
}
