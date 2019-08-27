package command;

import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Paths;

public abstract class ZipCommand implements Command {
    public ZipFileManager getZipFileManager() throws Exception {
        ConsoleHelper.writeMessage("Enter the full path to the archive:");
        String archivePath = ConsoleHelper.readString();
        return new ZipFileManager(Paths.get(archivePath));
    }
}
