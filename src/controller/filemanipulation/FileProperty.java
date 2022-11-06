package controller.filemanipulation;

import utils.PathUtils;

import java.io.File;
import java.text.SimpleDateFormat;

public class FileProperty {
    private final String path;
    private String name;
    private String typeAndExtension;
    private String size;
    private String lastModifiedDate;
    private Boolean[] read_write_execute;

    public FileProperty(String path) {
        this.path = path;
        System.out.println(path);
        readProperties();
    }

    private void readProperties() {
        File file = new File(path);
        name = file.getName();
        read_write_execute = new Boolean[3];
        read_write_execute[0] = file.canRead();
        read_write_execute[1] = file.canWrite();
        read_write_execute[2] = file.canExecute();
        size = String.valueOf(file.length());
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lastModifiedDate = date.format(file.lastModified());
        if (file.isDirectory()) {
            typeAndExtension = "Folder";
        } else {
            typeAndExtension = "File, " + PathUtils.getExtension(file);
        }
    }

    public String[][] getTableData() {
        String[][] data = new String[5][2];
        data[0][0] = "Name";
        data[0][1] = name;
        data[1][0] = "Type";
        data[1][1] = typeAndExtension;
        data[2][0] = "Size";
        data[2][1] = size;
        data[3][0] = "Last Modified";
        data[3][1] = lastModifiedDate;
        data[4][0] = "Read/Write/Execute";
        data[4][1] = read_write_execute[0] + "/" + read_write_execute[1] + "/" + read_write_execute[2];
        return data;
    }

    public String getName(){
        return name;
    }
}
