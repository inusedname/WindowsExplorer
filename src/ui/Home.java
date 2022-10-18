package ui;

import ui.treebrowse.TreeBrowse;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Home implements Runnable {
    private JPanel homePanel;
    private JScrollPane scrollPane;
    private JTree tree;
    private TreeBrowse treeBrowse;
    private JButton btBack;
    private JButton btForward;
    private JButton btUp;
    private JTable tableCurrentFolder;
    private JTextField tfAddress;
    private JButton btPicker;


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
        frame.setVisible(true);
    }

    private void setUpUI() {
        homePanel.setPreferredSize(new Dimension(1280, 720));
        setUpIcons();
    }

    private void setUpIcons() {
        btBack.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_back.png"))));
        btForward.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_forward.png"))));
        btUp.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_up.png"))));
        btPicker.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/drawable/ic_open_folder.png"))));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tree = new JTree();
        treeBrowse = new TreeBrowse(tree);
        scrollPane = new JScrollPane(tree);
    }

}
