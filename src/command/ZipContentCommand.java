package command;

import main.ConsoleHelper;
import main.ZipFileManager;

public class ZipContentCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Viewing archive content.");
        ZipFileManager manager = getZipFileManager();
        ConsoleHelper.writeMessage("Archive content:");
        manager.getFilesList().forEach(x -> ConsoleHelper.writeMessage(x.toString()));
        ConsoleHelper.writeMessage("Archive content has been read.\n");
    }
}
