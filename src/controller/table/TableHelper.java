package controller.table;

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
    private final TableHelper.TableCallbacks callbacks;
    private final String[] HEADER = new String[]{"Name", "Date modified", "Type", "Size"};
    public DefaultTableModel model = new DefaultTableModel();
    private String dir = "";

    public TableHelper(JTable table, TableCallbacks callbacks) {
        this.table = table;
        this.callbacks = callbacks;
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
                size = "" + fileSizeInKB + " kb";
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
                File file_1 = (File) table.getValueAt(table.rowAtPoint(mouseEvent.getPoint()), 0);
                String name = file_1.getName();
                String newDir;
                if ( !file_1.isDirectory() )
                {
                    newDir = dir;
                }
                else
                    newDir = String.format("%s%s\\", dir, name);
                if (mouseEvent.getButton() == MouseEvent.BUTTON1 && mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    File file = new File(newDir);
                    if (file.isDirectory()) {
                        callbacks.onTableFolderClicked(newDir);
                    } else {
                        callbacks.onTableFileClicked(newDir);
                    }
                }
                if (mouseEvent.getButton() == MouseEvent.BUTTON3 && table.getSelectedRow() != -1) {
                    PopupMenu menu = new TableRowPopupMenu(newDir);
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
            File file = (File)value;
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            label.setIcon(fileSystemView.getSystemIcon(file));
            label.setText(fileSystemView.getSystemDisplayName(file));
            return label;
        }


    }
}