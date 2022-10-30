package controller.table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;

public class TableHelper {

    private final JTable table;
    private final TableHelper.TableCallbacks callbacks;
    public DefaultTableModel model = new DefaultTableModel();
    private final String[] HEADER = new String[]{"Name", "Date modified", "Type", "Size"};
    private String dir = "";
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

    public TableHelper(JTable table, TableCallbacks callbacks) {
        this.table = table;
        this.callbacks = callbacks;
    }
    public void initTable() {

        JScrollPane scrollPane = new JScrollPane();
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setShowGrid(false);
        table.setAutoCreateRowSorter(true);
        scrollPane.setViewportView(table);
        model.setColumnIdentifiers(HEADER);
        table.setModel(model);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                String name = String.valueOf(table.getValueAt(row, 0));
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String newDir = dir + "\\" + name;
                    File file = new File(newDir);
                    if (file.isDirectory()) {
                        callbacks.onTableItemClicked(newDir);
                    }
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
        while (!file.isDirectory()) {
            file = file.getParentFile();
        }
        model = createTableData(file.toString());
        table.setModel(model);
        return true;
    }

    public interface TableCallbacks {
        void onTableItemClicked(String newDir);
    }

}