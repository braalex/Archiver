package main;

import exception.PathIsNotFoundException;
import exception.WrongZipFileException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    private void copyData(InputStream in, OutputStream out) throws IOException {
        while (in.available() > 0) {
            out.write(in.read());
        }
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        Path fullPath = filePath.resolve(fileName);
        try (InputStream in = Files.newInputStream(fullPath)) {
            ZipEntry entry = new ZipEntry(fileName.toString());
            zipOutputStream.putNextEntry(entry);
            copyData(in, zipOutputStream);
            zipOutputStream.closeEntry();
        }
    }

    public void createZip(Path source) throws Exception {
        Path zipPath = zipFile.getParent();
        if (Files.notExists(zipPath)) Files.createDirectory(zipPath);
        try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            if (Files.isRegularFile(source)) {
                addNewZipEntry(out, source.getParent(), source.getFileName());
            } else if (Files.isDirectory(source)) {
                FileManager manager = new FileManager(source);
                List<Path> fileNames = manager.getFileList();
                for (Path p : fileNames) addNewZipEntry(out, source, p);
            } else throw new PathIsNotFoundException();
        }
    }

    public List<FileProperties> getFilesList() throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();
        List<FileProperties> filesList = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile));
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                copyData(zis, baos);
                FileProperties prop =
                        new FileProperties(entry.getName(), entry.getSize(), entry.getCompressedSize(), entry.getMethod());
                filesList.add(prop);
                entry = zis.getNextEntry();
            }
        }
        return filesList;
    }

    public void extractAll(Path outputFolder) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();
        try (ZipInputStream in = new ZipInputStream(Files.newInputStream(zipFile))) {
            if (Files.notExists(outputFolder)) Files.createDirectories(outputFolder);
            ZipEntry entry = in.getNextEntry();
            while (entry != null) {
                Path fullPath = outputFolder.resolve(entry.getName());
                Path parent = fullPath.getParent();
                if (Files.notExists(parent)) Files.createDirectories(parent);
                try (OutputStream out = Files.newOutputStream(fullPath)) {
                    copyData(in, out);
                    in.closeEntry();
                }
                entry = in.getNextEntry();
            }

        }
    }

    public void removeFiles(List<Path> pathList) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();
        Path tmp = Files.createTempFile(null, null);
        try (ZipInputStream in = new ZipInputStream(Files.newInputStream(zipFile));
             ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(tmp))) {
            ZipEntry entry = in.getNextEntry();
            while (entry != null) {
                if (pathList.contains(Paths.get(entry.getName())))
                    ConsoleHelper.writeMessage("File " + entry.getName() + " was removed.");
                else {
                    out.putNextEntry(new ZipEntry(entry.getName()));
                    copyData(in, out);
                    out.closeEntry();
                    in.closeEntry();
                }
                entry = in.getNextEntry();
            }
        }
        Files.move(tmp, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    public void addFiles(List<Path> absolutePathList) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();
        Path tmp = Files.createTempFile(null, null);
        List<Path> filesList = new ArrayList<>();
        try (ZipInputStream in = new ZipInputStream(Files.newInputStream(zipFile));
             ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(tmp))) {
            ZipEntry entry = in.getNextEntry();
            while (entry != null) {
                out.putNextEntry(new ZipEntry(entry.getName()));
                copyData(in, out);
                out.closeEntry();
                in.closeEntry();
                filesList.add(Paths.get(entry.getName()));
                entry = in.getNextEntry();
            }

            for (Path path : absolutePathList) {
                if (Files.exists(path) && Files.isRegularFile(path)) {
                    if (!filesList.contains(path)) {
                        addNewZipEntry(out, path.getParent(), path.getFileName());
                        filesList.add(path.getFileName());
                        ConsoleHelper.writeMessage("File " + path.getFileName() + " was added to the archive.");
                    } else
                        ConsoleHelper.writeMessage("File " + path.getFileName() + " already contains in the archive.");
                } else throw new PathIsNotFoundException();
            }
        }
        Files.move(tmp, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void addFile(Path absolutePath) throws Exception {
        addFiles(Collections.singletonList(absolutePath));
    }
}
