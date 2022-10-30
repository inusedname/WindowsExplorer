package ui;


import com.formdev.flatlaf.FlatDarculaLaf;
import controller.historystack.HistoryHelper;
import controller.table.TableHelper;
import controller.treebrowse.TreeHelper;
import utils.PathUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class Home implements Runnable, TreeHelper.TreeCallbacks, TableHelper.TableCallbacks {
    private final HistoryHelper historyHelper;
    private JPanel homePanel;
    private JTree tree;
    private TreeHelper treeHelper;
    private JButton btBack;
    private JButton btForward;
    private JButton btUp;
    private JTable tableCurrentFolder;
    private TableHelper tableHelper;
    private JTextField tfAddress;

    public Home() {
        historyHelper = new HistoryHelper();
        setUpUI();
        setUpActionListeners();
        setUpObservers();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Home());
    }

    private void setUpObservers() {
        historyHelper.addHistoryChangeListener(evt -> {
            if (evt.getPropertyName().equals("currentHistory")) {
                int newHistoryIndex = (int) evt.getNewValue();
                btBack.setEnabled(newHistoryIndex > 0);
                btForward.setEnabled(newHistoryIndex < historyHelper.getHistoryStackSize() - 1);
                btUp.setEnabled(PathUtils.getParentFolder(historyHelper.getCurrentHistory().getPath()) != null);
            }
        });
    }

    private void setUpActionListeners() {
        setUpTextFieldActionListener();
        setUpNavigationBarActionListener();
    }

    private void setUpNavigationBarActionListener() {
        btBack.addActionListener(e -> {
            String newPath = historyHelper.goToPreviousHistory().getPath();
            updateTreeTableAndTF(newPath);
        });

        btForward.addActionListener(e -> {
            String newPath = historyHelper.goToNextHistory().getPath();
            updateTreeTableAndTF(newPath);
        });

        btUp.addActionListener(e -> {
            historyHelper.pushBackHistory(PathUtils.getParentFolder(tfAddress.getText()));
            updateTreeTableAndTF(historyHelper.getCurrentHistory().getPath());
        });
    }

    private void setUpTextFieldActionListener() {
        tfAddress.addActionListener(e -> {
            historyHelper.pushBackHistory(tfAddress.getText());
            updateTreeTableAndTF(tfAddress.getText());
        });
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Home");
        frame.setContentPane(new Home().homePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setUpUI() {

        FlatDarculaLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        homePanel.setPreferredSize(new Dimension(1280, 720));
        btBack.setEnabled(false);
        btForward.setEnabled(false);
        btUp.setEnabled(false);
        setUpIcons();
    }

    private void setUpIcons() {

        ImageIcon iconBack = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_back_1.png")));
        ImageIcon iconForward = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_forward.png")));
        ImageIcon iconUp = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_up.png")));

        btBack.setIcon(iconBack);
        btForward.setIcon(iconForward);
        btUp.setIcon(iconUp);
    }

    private void createUIComponents() {
        setUpJTree();
        setUpJTable();
    }

    private void setUpJTree() {
        tree = new JTree();
        treeHelper = new TreeHelper(tree, this);
        treeHelper.initTree();
    }

    private void setUpJTable() {
        tableCurrentFolder = new JTable();
        tableHelper = new TableHelper(tableCurrentFolder, this);
        tableHelper.initTable();
    }

    @Override
    public void onTreeItemClicked(String newDir) {
        historyHelper.pushBackHistory(newDir);
        tfAddress.setText(newDir);
        tableHelper.goToPath(newDir);
    }

    @Override
    public void onTableItemClicked(String newDir) {
        historyHelper.pushBackHistory(newDir);
        tfAddress.setText(newDir);
        treeHelper.goToPath(newDir);
    }

    private void updateTreeTableAndTF(String newDir) {
        if (!new File(newDir).exists()) {
            JOptionPane.showMessageDialog(homePanel, "Invalid path");
            return;
        }
        tfAddress.setText(newDir);
        treeHelper.goToPath(newDir);
        tableHelper.goToPath(newDir);
    }
}
