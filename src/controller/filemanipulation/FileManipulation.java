package controller.filemanipulation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileManipulation {

    public static String tmpPath = null;
    public static String tmpName = null;
    public static TmpMode tmpMode = null;

    public static void deleteFile(String path) {
        try {
            new File(path).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void renameFile(String oldPath, String newPath) {
        try {
            new File(oldPath).renameTo(new File(newPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void moveFile(String oldPath, String newPath) {
        try {
            Files.createDirectories(new File(newPath).toPath().getParent());
            Files.move(new File(oldPath).toPath(), new File(newPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            Files.createDirectories(new File(newPath).toPath().getParent());
            Files.copy(new File(oldPath).toPath(), new File(newPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception ignored) {
        }
    }

    public enum TmpMode {
        COPY, CUT
    }
}

