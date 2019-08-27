package command;

import exception.PathIsNotFoundException;
import exception.WrongZipFileException;
import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Paths;

public class ZipAddCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Adding to the archive.");
            ZipFileManager manager = getZipFileManager();
            ConsoleHelper.writeMessage("Enter the full path to the file to be added:");
            String fileName = ConsoleHelper.readString();
            manager.addFile(Paths.get(fileName));
            ConsoleHelper.writeMessage("Adding completed.\n");
        } catch (WrongZipFileException e) {
            ConsoleHelper.writeMessage("Wrong archive.\n");
        } catch (PathIsNotFoundException e1) {
            ConsoleHelper.writeMessage("You entered an incorrect file or directory name.\n");
        }
    }
}
