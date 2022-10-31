package controller.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
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
            tb.addRow(new Object[]{name, misclassified, type, size});
        }
        return tb;
    }

    public void initTable() {

        JScrollPane scrollPane = new JScrollPane();
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setShowGrid(false);
        table.setAutoCreateRowSorter(true);
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(JLabel.LEFT);
        scrollPane.setViewportView(table);
        model.setColumnIdentifiers(HEADER);
        table.setDefaultEditor(Object.class, null);
        table.setModel(model);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                String name = String.valueOf(table.getValueAt(table.rowAtPoint(mouseEvent.getPoint()), 0));
                String newDir = String.format("%s%s\\", dir, name);
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
        return true;
    }

    public interface TableCallbacks {
        void onTableFolderClicked(String newDir);
        void onTableFileClicked(String newDir);
    }

}