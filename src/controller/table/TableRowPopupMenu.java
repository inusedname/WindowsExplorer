package controller.table;

import ui.FilePropertyDialog;

public class TableRowPopupMenu extends PopupMenu {
    public TableRowPopupMenu(String path) {
        add("Cut").addActionListener(e -> {

        });
        add("Move").addActionListener(e -> {

        });
        add("Copy").addActionListener(e -> {

        });
        add("Delete").addActionListener(e -> {

        });
        add("Rename").addActionListener(e -> {

        });
        add("Properties").addActionListener(e -> {
            FilePropertyDialog dialog = new FilePropertyDialog(path);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
    }
}

