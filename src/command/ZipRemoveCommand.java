package command;

import exception.WrongZipFileException;
import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Paths;

public class ZipRemoveCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Removing from the archive.");
            ZipFileManager manager = getZipFileManager();
            ConsoleHelper.writeMessage("Enter the name of the file to be removed:");
            String fileName = ConsoleHelper.readString();
            manager.removeFile(Paths.get(fileName));
            ConsoleHelper.writeMessage("Removing completed.\n");
        } catch (WrongZipFileException e) {
            ConsoleHelper.writeMessage("Wrong archive.\n");
        }
    }
}
