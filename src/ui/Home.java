package ui;


import com.formdev.flatlaf.FlatDarculaLaf;
import controller.table.TableHelper;
import controller.historystack.HistoryHelper;
import controller.treebrowse.TreeHelper;
import utils.PathUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Home implements Runnable, TreeHelper.TreeCallbacks, TableHelper.TableCallbacks {
    private JPanel homePanel;
    private JTree tree;
    private JButton btBack;
    private JButton btForward;
    private JButton btUp;
    private JTable tableCurrentFolder;

    private TableHelper tableHelper;

    private JTextField tfAddress;
    private JButton btPicker;
    private final HistoryHelper historyHelper = new HistoryHelper();
    private final TreeHelper treeHelper = new TreeHelper(tree, this);

    public Home() {
        setUpUI();
        setUpActionListeners();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Home());
    }

    private void setUpActionListeners() {
        setUpTextFieldActionListener();
        setUpNavigationBarActionListener();
    }

    private void setUpNavigationBarActionListener() {
        btBack.addActionListener(e -> {
            String newPath = historyHelper.getPreviousHistory().getPath();
            // TODO: Not yet implement
        });

        btForward.addActionListener(e -> {
            String newPath = historyHelper.getNextHistory().getPath();
            // TODO: Not yet implement
        });

        btUp.addActionListener(e -> {
            historyHelper.pushBackHistory(PathUtils.getParentFolder(tfAddress.getText()));
        });
    }

    private void setUpTextFieldActionListener() {
        tfAddress.addActionListener(e -> {
            String path = tfAddress.getText();
            System.out.println("Path: " + path);

            if (!treeHelper.goToPath(path)) {
                JOptionPane.showMessageDialog(homePanel, "Invalid path");
            }
            if (!tableHelper.goToPath(path)) {
                JOptionPane.showMessageDialog(homePanel, "Invalid path");
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
        setUpIcons();
    }

    private void setUpIcons() {

        ImageIcon iconBack = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_back.png")));
        ImageIcon iconForward = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_forward.png")));
        ImageIcon iconUp = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_up.png")));
        ImageIcon iconPicker = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_open_folder.png")));

        btBack.setIcon(iconBack);
        btForward.setIcon(iconForward);
        btUp.setIcon(iconUp);
        btPicker.setIcon(iconPicker);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        setUpJTree();
        setUpJTable();
    }

    private void setUpJTree() {
        tree = new JTree();
        tree.setToggleClickCount(1);
        tree.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        treeHelper.initTree();
    }

    private void setUpJTable() {
        tableCurrentFolder = new JTable();
        tableHelper = new TableHelper(tableCurrentFolder, this);
        tableHelper.initTable();

    }

    @Override
    public void onTreeClicked(String newDir) {
        tfAddress.setText(newDir);
        tableHelper.goToPath(newDir);
    }

    @Override
    public void onTableClicked(String newDir) {
        tfAddress.setText(newDir);
        tableHelper.goToPath(newDir);
    }
}
