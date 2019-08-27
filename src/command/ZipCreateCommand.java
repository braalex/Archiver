package command;

import exception.PathIsNotFoundException;
import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Paths;

public class ZipCreateCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Archive creation.");
            ZipFileManager manager = getZipFileManager();
            ConsoleHelper.writeMessage("Enter the full path to the file or directory for archiving:");
            String sourcePath = ConsoleHelper.readString();
            manager.createZip(Paths.get(sourcePath));
            ConsoleHelper.writeMessage("Archive created.\n");
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("You entered an incorrect file or directory name.\n");
        }
    }
}
