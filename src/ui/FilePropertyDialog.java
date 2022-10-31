package ui;

import controller.filemanipulation.FileProperty;

import javax.swing.*;

public class FilePropertyDialog extends JDialog {
    private final String path;
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable tableProperty;

    public FilePropertyDialog(String path) {
        this.path = path;
        setTitle("File Properties");
        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new java.awt.Dimension(300, 200));
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void createUIComponents() {
        setUpTable();
    }

    private void setUpTable() {
        tableProperty = new JTable(new FileProperty(path).getTableData(), new String[]{"Property", "Value"});
    }
}
