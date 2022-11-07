package ui;


import com.formdev.flatlaf.FlatDarculaLaf;
import controller.historystack.HistoryHelper;
import controller.table.TableHelper;
import controller.treebrowse.TreeHelper;
import interfaces.FileManipulationDialogCallback;
import utils.PathUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class Home implements Runnable,
        TreeHelper.TreeCallbacks,
        TableHelper.TableCallbacks,
        FileManipulationDialogCallback {
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
    private JButton btBuyMeACoffee;

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
        setUpBottomBarActionListener();
    }

    private void setUpBottomBarActionListener() {
        btBuyMeACoffee.addActionListener(e -> {
            BuyMeACoffeeDialog dialog = new BuyMeACoffeeDialog();
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
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
            String newPath = PathUtils.getParentFolder(tfAddress.getText());
            if (updateTreeTableAndTF(newPath)) {
                historyHelper.pushBackHistory(newPath);
            }
        });
    }

    private void setUpTextFieldActionListener() {
        tfAddress.addActionListener(e -> {
            File file = new File(tfAddress.getText());
            if (!file.isDirectory()) file = file.getParentFile();
            if (updateTreeTableAndTF(file.toString() + "\\")) {
                historyHelper.pushBackHistory(file + "\\");
            }
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
        tableHelper = new TableHelper(tableCurrentFolder, this, this);
        tableHelper.initTable();
    }

    @Override
    public void onTreeItemClicked(String newDir) {
        if (tableHelper.goToPath(newDir)) {
            tfAddress.setText(newDir);
            historyHelper.pushBackHistory(newDir);
        }
    }

    @Override
    public void onTableFolderClicked(String newDir) {
        if (updateTreeTableAndTF(newDir)) {
            System.out.println("Hello1");
            historyHelper.pushBackHistory(newDir);
        }
    }

    @Override
    public void onTableFileClicked(String newDir) {

    }

    private boolean updateTreeTableAndTF(String newDir) {
        if (!new File(newDir).exists()) {
            JOptionPane.showMessageDialog(homePanel, "Invalid path");
            return false;
        }
        if (tableHelper.goToPath(newDir)) {
            tfAddress.setText(newDir);
            treeHelper.goToPath(newDir);
            return true;
        }
        return false;
    }

    @Override
    public void onOk() {
        tableHelper.goToPath(historyHelper.getCurrentHistory().getPath());
        treeHelper.goToPath(historyHelper.getCurrentHistory().getPath());
    }
}
