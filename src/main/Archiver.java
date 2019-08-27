package main;

import exception.WrongZipFileException;
import java.io.IOException;

public class Archiver {
    public static Operation askOperation() throws IOException {
        ConsoleHelper.writeMessage("Choose operation from the list and print number:\n" +
                Operation.CREATE.ordinal() + ". Pack files to archive\n" +
                Operation.ADD.ordinal() + ". Add file to archive\n" +
                Operation.REMOVE.ordinal() + ". Remove file from archive\n" +
                Operation.EXTRACT.ordinal() + ". Extract files from archive\n" +
                Operation.CONTENT.ordinal() + ". View archive contents\n" +
                Operation.EXIT.ordinal() + ". Exit");
        return Operation.values()[ConsoleHelper.readInt()];
    }

    public static void main(String[] args) {
        Operation operation = null;
        do {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (WrongZipFileException zipE) {
                ConsoleHelper.writeMessage("You did not select the archive file or selected the wrong file.\n");
            } catch (Exception e) {
                ConsoleHelper.writeMessage("An error has occurred. Check your input data.\n");
            }
        }
        while (operation != Operation.EXIT);
    }
}
