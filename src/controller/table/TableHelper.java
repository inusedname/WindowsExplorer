package controller.table;

import interfaces.FileManipulationDialogCallback;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;

public class TableHelper {

    private final JTable table;
    private final TableHelper.TableCallbacks tableCallback;
    private final FileManipulationDialogCallback fileManipulationDialogCallback;
    private final String[] HEADER = new String[]{"Name", "Date modified", "Type", "Size"};
    public DefaultTableModel model = new DefaultTableModel();
    private String dir = "";

    public TableHelper(JTable table, TableCallbacks tableCallbacks, FileManipulationDialogCallback dialogCallback) {
        this.table = table;
        this.tableCallback = tableCallbacks;
        this.fileManipulationDialogCallback = dialogCallback;
    }

    private DefaultTableModel createTableData(String folder) {
        DefaultTableModel tb = new DefaultTableModel();
        tb.setColumnIdentifiers(HEADER);
        if (tb.getRowCount() > 0) {
            for (int i = tb.getRowCount() - 1; i > -1; i--) {
                tb.removeRow(i);
            }
        }
        File file = new File(folder);
        File[] list = file.listFiles();
        assert list != null;
        for (File files : list) {
            if (files.isHidden())
                continue;
            String name = files.getName();
            String type = "";
            String size = "";
            if (files.isFile()) {
                type = "." + name.substring(name.lastIndexOf(".") + 1);
                long fileSizeInBytes = files.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                size = "" + fileSizeInKB + " KB";
            }
            if (files.isDirectory()) {
                type = "Folder";
            }
            SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String misclassified = date.format(files.lastModified());
            tb.addRow(new Object[]{files, misclassified, type, size});
        }

        return tb;
    }

    public void initTable() {

        JScrollPane scrollPane = new JScrollPane();
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setShowGrid(false);
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(JLabel.LEFT);
        scrollPane.setViewportView(table);
        model.setColumnIdentifiers(HEADER);
        table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
        table.setModel(model);

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                String newDir;
                if (table.rowAtPoint(mouseEvent.getPoint()) != -1) {
                    String path = String.valueOf(table.getValueAt(table.rowAtPoint(mouseEvent.getPoint()), 0));
                    newDir = String.format("%s\\", path);
                } else {
                    newDir = dir;
                }
                if (mouseEvent.getButton() == MouseEvent.BUTTON1 && mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    File file = new File(newDir);
                    if (file.isDirectory()) {
                        tableCallback.onTableFolderClicked(newDir);
                    } else {
                        tableCallback.onTableFileClicked(newDir);
                    }
                }
                if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                    PopupMenu menu = new TableRowPopupMenu(newDir, fileManipulationDialogCallback);
                    menu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                }
            }
        });
    }

    public boolean goToPath(String path) {
        dir = path;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (file.listFiles() == null) {
            JOptionPane.showMessageDialog(table, "You don't have permission to access this folder", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        while (!file.isDirectory()) {
            file = file.getParentFile();
        }
        model = createTableData(file.toString());
        table.setModel(model);
        table.getColumnModel().getColumn(0).setCellRenderer(new Renderer());
        return true;
    }

    public interface TableCallbacks {
        void onTableFolderClicked(String newDir);

        void onTableFileClicked(String newDir);
    }

    private static class Renderer extends DefaultTableCellRenderer {
        private final FileSystemView fileSystemView;

        private Renderer() {
            fileSystemView = FileSystemView.getFileSystemView();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            File file = (File) value;
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            label.setIcon(fileSystemView.getSystemIcon(file));
            label.setText(fileSystemView.getSystemDisplayName(file));
            return label;
        }


    }
}