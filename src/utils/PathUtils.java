package utils;

import java.io.File;

public class PathUtils {
    public static String getParentFolder(String path) {
        if (path == null) {
            return null;
        }
        path = path.substring(0, path.length() - 1);
        if (path.length() == 0) {
            return null;
        }
        if (path.length() == 1) {
            return null;
        }
        int lastSlashIndex = path.lastIndexOf('\\');
        if (lastSlashIndex == -1) {
            return null;
        }
        return path.substring(0, lastSlashIndex + 1);
    }

    public static String getExtension(File file) {
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return null;
        }
        return name.substring(lastDotIndex + 1);
    }
}
