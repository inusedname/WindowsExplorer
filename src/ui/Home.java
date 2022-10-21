package ui;

import com.formdev.flatlaf.FlatDarculaLaf;
import controller.treebrowse.TreeHelper;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Home implements Runnable, TreeHelper.TreeCallbacks {
    private JPanel homePanel;
    private JTree tree;
    private JButton btBack;
    private JButton btForward;
    private JButton btUp;
    private JTable tableCurrentFolder;
    private JTextField tfAddress;
    private JButton btPicker;
    private JScrollPane leftScrollPane;

    public Home() {
        setUpUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Home());
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
        tree.setToggleClickCount(1);
        tree.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
        tree = new JTree();
        TreeHelper treeHelper = new TreeHelper(tree, this);
        treeHelper.initTree();
    }

    @Override
    public void onTreeClicked(String newDir) {
        tfAddress.setText(newDir);
    }
}
