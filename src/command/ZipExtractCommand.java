package command;

import exception.PathIsNotFoundException;
import exception.WrongZipFileException;
import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Paths;

public class ZipExtractCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Archive extraction.");
            ZipFileManager manager = getZipFileManager();
            ConsoleHelper.writeMessage("Enter the full path to the directory for extracting:");
            String outputPath = ConsoleHelper.readString();
            manager.extractAll(Paths.get(outputPath));
            ConsoleHelper.writeMessage("Archive extracted.\n");
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("You entered an incorrect file or directory name.\n");
        } catch (WrongZipFileException e1) {
            ConsoleHelper.writeMessage("Wrong archive.\n");
        }
    }
}
