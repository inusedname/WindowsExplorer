package controller.table;

import controller.filemanipulation.FileManipulation;
import ui.FilePropertyDialog;

public class TableRowPopupMenu extends PopupMenu {
    public TableRowPopupMenu(String path) {
        add("Cut").addActionListener(e -> {
            FileManipulation.tmpPath = path;
            FileManipulation.tmpMode = FileManipulation.TmpMode.CUT;
            // TODO: Not yet implement
        });
        add("Move").addActionListener(e -> {
            // TODO: Not yet implement
        });
        add("Copy").addActionListener(e -> {
            FileManipulation.tmpPath = path;
            FileManipulation.tmpMode = FileManipulation.TmpMode.COPY;
            // TODO: Not yet implement
        });
        add("Delete").addActionListener(e -> {
            // TODO: Not yet implement
        });
        add("Rename").addActionListener(e -> {
            // TODO: Not yet implement
        });
        add("Properties").addActionListener(e -> {
            FilePropertyDialog dialog = new FilePropertyDialog(path);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
        addPasteOptionIfAvailable();
    }

    public void addPasteOptionIfAvailable() {
        boolean available = FileManipulation.tmpMode != null && FileManipulation.tmpPath != null;
        if (available) {
            add("Paste").addActionListener(e -> {
                switch (FileManipulation.tmpMode) {
                    case CUT -> {
                        // 1
                    }
                    case COPY -> {
                        // 2
                    }
                }
                // TODO: Not yet implemented
                FileManipulation.tmpPath = null;
                FileManipulation.tmpMode = null;
            });
        }
    }
}

