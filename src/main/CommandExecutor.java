package main;

import command.*;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private CommandExecutor() {
    }

    private static final Map<Operation, Command> allKnownCommands = new HashMap<>();
    static {
            allKnownCommands.put(Operation.CREATE, new ZipCreateCommand());
            allKnownCommands.put(Operation.CONTENT, new ZipContentCommand());
            allKnownCommands.put(Operation.EXTRACT, new ZipExtractCommand());
            allKnownCommands.put(Operation.REMOVE, new ZipRemoveCommand());
            allKnownCommands.put(Operation.ADD, new ZipAddCommand());
            allKnownCommands.put(Operation.EXIT, new ExitCommand());
    }

    public static void execute(Operation operation) throws Exception {
        Command command = allKnownCommands.get(operation);
        command.execute();
    }
}
