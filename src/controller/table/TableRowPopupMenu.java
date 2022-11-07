package controller.table;

import controller.filemanipulation.FileManipulation;
import interfaces.FileManipulationDialogCallback;
import ui.FilePropertyDialog;
import ui.MoveFileDialog;
import ui.RenameFileDialog;
import utils.PathUtils;

public class TableRowPopupMenu extends PopupMenu {

    private final FileManipulationDialogCallback callback;

    public TableRowPopupMenu(String path, FileManipulationDialogCallback callback) {
        this.callback = callback;
        add("Cut").addActionListener(e -> {
            FileManipulation.tmpName = PathUtils.getFileName(path);
            FileManipulation.tmpPath = path;
            FileManipulation.tmpMode = FileManipulation.TmpMode.CUT;
        });
        add("Move").addActionListener(e -> {
            MoveFileDialog dialog = new MoveFileDialog(path, this.callback);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
        add("Copy").addActionListener(e -> {
            FileManipulation.tmpName = PathUtils.getFileName(path);
            FileManipulation.tmpPath = path;
            FileManipulation.tmpMode = FileManipulation.TmpMode.COPY;
        });
        add("Delete").addActionListener(e -> FileManipulation.deleteFile(path));
        add("Rename").addActionListener(e -> {
            RenameFileDialog dialog = new RenameFileDialog(path, this.callback);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
        addPasteOption(path + FileManipulation.tmpName);
        add("Properties").addActionListener(e -> {
            FilePropertyDialog dialog = new FilePropertyDialog(path);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
    }

    public void addPasteOption(String path) {
        boolean available = FileManipulation.tmpMode != null && FileManipulation.tmpPath != null;
        if (available) {
            add("Paste").addActionListener(e -> {
                switch (FileManipulation.tmpMode) {
                    case CUT -> FileManipulation.moveFile(FileManipulation.tmpPath, path);
                    case COPY -> FileManipulation.copyFile(FileManipulation.tmpPath, path);
                }
                FileManipulation.tmpPath = null;
                FileManipulation.tmpMode = null;
            });
        }
    }
}

