package controller.table;

import controller.filemanipulation.FileManipulation;
import controller.filemanipulation.FileProperty;
import ui.FilePropertyDialog;
import ui.MoveFileDialog;
import ui.RenameFileDialog;

public class TableRowPopupMenu extends PopupMenu {
    public TableRowPopupMenu(String path) {
        FileProperty file = new FileProperty(path);
        add("Cut").addActionListener(e -> {
            FileManipulation.tmpName = file.getName();
            FileManipulation.tmpPath = path;
            FileManipulation.tmpMode = FileManipulation.TmpMode.CUT;
        });
        add("Move").addActionListener(e -> {
            MoveFileDialog dialog = new MoveFileDialog(path,file.getName());
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
        add("Copy").addActionListener(e -> {
            FileManipulation.tmpName = file.getName();
            FileManipulation.tmpPath = path;
            FileManipulation.tmpMode = FileManipulation.TmpMode.COPY;
        });
        add("Delete").addActionListener(e -> {
            FileManipulation fileManipulation = new FileManipulation();
            fileManipulation.deleteFile(path);
        });
        add("Rename").addActionListener(e -> {
            RenameFileDialog dialog = new RenameFileDialog(path,file.getName());
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
        add("Properties").addActionListener(e -> {
            FilePropertyDialog dialog = new FilePropertyDialog(path);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
        addPasteOptionIfAvailable(path+FileManipulation.tmpName);
    }

    public void addPasteOptionIfAvailable(String path) {
        boolean available = FileManipulation.tmpMode != null && FileManipulation.tmpPath != null;
        if (available) {
            add("Paste").addActionListener(e -> {
                switch (FileManipulation.tmpMode) {
                    case CUT -> {
                        FileManipulation fileManipulation = new FileManipulation();
                        fileManipulation.moveFile(FileManipulation.tmpPath,path);
                    }
                    case COPY -> {
                        FileManipulation fileManipulation = new FileManipulation();
                        fileManipulation.copyFile(FileManipulation.tmpPath,path);
                    }
                }
                FileManipulation.tmpPath = null;
                FileManipulation.tmpMode = null;
            });
        }
    }
}

