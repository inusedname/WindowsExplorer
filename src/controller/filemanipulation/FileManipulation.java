package controller.filemanipulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManipulation {

    public boolean deleteFile(String path) {
        try {
            return new File(path).delete();
        } catch (Exception ignored) {
        }
        return false;
    }

    public boolean renameFile(String oldPath, String newPath) {
        try {
            return new File(oldPath).renameTo(new File(newPath));
        } catch (Exception ignored) {
        }
        return false;
    }

    public void moveFile(String oldPath, String newPath) {
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(oldPath);
            out = new FileOutputStream(newPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();

            new File(oldPath).delete();
        } catch (Exception ignored) {
        }
    }

    public void copyFile(String oldPath, String newPath) {

        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(oldPath);
            out = new FileOutputStream(newPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();

        } catch (Exception ignored) {
        }
    }
}
