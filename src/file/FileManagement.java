package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManagement {

    public void deleteFile(String path) {
        try {
            // delete the original file
            new File(path).delete();
        } catch (Exception e) {
            //Log.e("tag", e.getMessage());
        }
    }

    public void renameFile(String oldPath, String newPath) {
        try {
            // rename the original file
            new File(oldPath).renameTo(new File(newPath));
        } catch (Exception e) {
            //Log.e("tag", e.getMessage());
        }
    }

    public void moveFile(String oldPath, String newPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(oldPath);
            out = new FileOutputStream(newPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(oldPath).delete();

        } catch (FileNotFoundException fnfe1) {
            //Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            //Log.e("tag", e.getMessage());
        }
    }

    public void copyFile(String oldPath, String newPath) {

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(oldPath);
            out = new FileOutputStream(newPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            //Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            //Log.e("tag", e.getMessage());
        }

    }

}

