package utils;

public class PathUtils {
    public static String getParentFolder(String path) {
        if (path == null) {
            return null;
        }
        if (path.length() == 0) {
            return null;
        }
        if (path.length() == 1) {
            return null;
        }
        if (path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1);
        }
        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            return null;
        }
        return path.substring(0, lastSlashIndex);
    }
}
